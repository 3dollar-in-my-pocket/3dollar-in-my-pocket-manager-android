package app.threedollars.manager.feature.storemanagement.components.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.threedollars.common.Resource
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import app.threedollars.domain.usecase.CreateNonceUseCase
import app.threedollars.domain.usecase.GetStoreMessageListUseCase
import app.threedollars.domain.usecase.SendStoreMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MessageViewModel @Inject constructor(
    private val bossStoreRetrieveUseCase: BossStoreRetrieveUseCase,
    private val getStoreMessageListUseCase: GetStoreMessageListUseCase,
    private val sendStoreMessageUseCase: SendStoreMessageUseCase,
    private val createNonceUseCase: CreateNonceUseCase,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(MessageState())
    val stateFlow: StateFlow<MessageState> = _stateFlow.asStateFlow()

    private val _sentFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val sentFlow = _sentFlow.asSharedFlow()

    fun onTabEntered(storeId: String) {
        if (storeId.isEmpty()) return
        _stateFlow.update { it.copy(storeId = storeId) }
        loadFirstPage(pullToRefresh = false)
    }

    fun refresh() {
        loadFirstPage(pullToRefresh = true)
    }

    private fun loadFirstPage(pullToRefresh: Boolean) {
        val state = _stateFlow.value
        if (state.storeId.isEmpty() || state.isLoading || state.isRefreshing) return
        _stateFlow.update { if (pullToRefresh) it.copy(isRefreshing = true) else it.copy(isLoading = true) }
        viewModelScope.launch {
            val storeDeferred = async { bossStoreRetrieveUseCase.getBossStoreRetrieveMe().first() }
            val messagesDeferred = async { getStoreMessageListUseCase(storeId = state.storeId, size = MESSAGE_PAGE_SIZE) }
            val storeResult = storeDeferred.await()
            val messagesResult = messagesDeferred.await()
            val store = storeResult.data
            val messages = messagesResult.data
            if (storeResult !is Resource.Success || store == null) {
                _stateFlow.update { it.copy(isLoading = false, isRefreshing = false, error = MessageError(storeResult.errorMessage)) }
                return@launch
            }
            if (messagesResult !is Resource.Success || messages == null) {
                _stateFlow.update { it.copy(isLoading = false, isRefreshing = false, error = MessageError(messagesResult.errorMessage)) }
                return@launch
            }
            _stateFlow.update {
                it.withFirstPage(
                    storeName = store.name.orEmpty(),
                    subscriberCount = store.favoriteDto?.subscriberCount ?: 0,
                    page = messages.contents,
                    nextCursor = messages.cursor?.nextCursor,
                    hasMore = messages.cursor?.hasMore ?: false,
                    policy = messages.policy,
                )
            }
        }
    }

    fun loadNextPage() {
        val state = _stateFlow.value
        if (!state.canLoadMore) return
        _stateFlow.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            val result = getStoreMessageListUseCase(storeId = state.storeId, size = MESSAGE_PAGE_SIZE, cursor = state.nextCursor)
            val data = result.data
            if (result is Resource.Success && data != null) {
                _stateFlow.update {
                    it.withNextPage(page = data.contents, nextCursor = data.cursor?.nextCursor, hasMore = data.cursor?.hasMore ?: false)
                }
            } else {
                _stateFlow.update { it.copy(isLoadingMore = false, error = MessageError(result.errorMessage)) }
            }
        }
    }

    fun openCompose(body: String = "") {
        _stateFlow.update { it.copy(compose = MessageComposeState(body = body), confirm = null) }
    }

    fun dismissCompose() {
        _stateFlow.update { it.copy(compose = null) }
    }

    fun updateBody(body: String) {
        _stateFlow.update { it.copy(compose = it.compose?.withBody(body)) }
    }

    fun onInputFocusChanged(focused: Boolean) {
        _stateFlow.update { it.copy(compose = it.compose?.withFocusChanged(focused)) }
    }

    fun onInputTouched() {
        _stateFlow.update { it.copy(compose = it.compose?.withTouched()) }
    }

    fun submitCompose() {
        val compose = _stateFlow.value.compose ?: return
        if (!compose.isValid) {
            _stateFlow.update { it.copy(compose = compose.withValidationError()) }
            return
        }
        _stateFlow.update { it.copy(compose = null, confirm = MessageConfirmState(body = compose.body)) }
        requestNonce()
    }

    private fun requestNonce() {
        viewModelScope.launch {
            val result = createNonceUseCase()
            val nonce = result.data
            if (result is Resource.Success && !nonce.isNullOrEmpty()) {
                _stateFlow.update { it.copy(confirm = it.confirm?.copy(nonce = nonce)) }
            } else {
                _stateFlow.update { it.copy(error = MessageError(result.errorMessage)) }
            }
        }
    }

    fun rewrite() {
        val confirm = _stateFlow.value.confirm ?: return
        openCompose(body = confirm.body)
    }

    fun dismissConfirm() {
        _stateFlow.update { it.copy(confirm = null) }
    }

    fun send() {
        val state = _stateFlow.value
        val confirm = state.confirm ?: return
        if (!confirm.canSend) return
        val nonce = confirm.nonce ?: return
        _stateFlow.update { it.copy(confirm = confirm.copy(isSending = true)) }
        viewModelScope.launch {
            val result = sendStoreMessageUseCase(storeId = state.storeId, nonce = nonce, body = confirm.body)
            val created = result.data
            if (result is Resource.Success && created != null) {
                _stateFlow.update { it.withMessageSent(created) }
                _sentFlow.emit(Unit)
            } else {
                _stateFlow.update {
                    it.copy(confirm = it.confirm?.copy(isSending = false), error = MessageError(result.errorMessage))
                }
            }
        }
    }

    fun clearError() {
        _stateFlow.update { it.copy(error = null) }
    }
}
