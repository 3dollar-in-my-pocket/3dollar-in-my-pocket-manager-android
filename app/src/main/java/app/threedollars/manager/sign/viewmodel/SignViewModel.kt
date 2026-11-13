package app.threedollars.manager.sign.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.usecase.AuthUseCase
import app.threedollars.domain.usecase.BossDeviceUseCase
import app.threedollars.domain.usecase.ImageUploadUseCase
import app.threedollars.domain.usecase.PlatformStoreCategoryUseCase
import app.threedollars.manager.feature.storemanagement.model.StoreCategoriesVo
import app.threedollars.manager.sign.KakaoTokenProvider
import app.threedollars.manager.sign.LoginNavItem
import app.threedollars.manager.util.dtoToVo
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class SignViewModel @Inject constructor(
    private val platformStoreCategoryUseCase: PlatformStoreCategoryUseCase,
    private val authUseCase: AuthUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
    private val bossDeviceUseCase: BossDeviceUseCase,
    private val kakaoTokenProvider: KakaoTokenProvider,
) : BaseViewModel() {

    val categoryItemState = mutableStateListOf<StoreCategoriesVo>()

    private val selectedItems = mutableListOf<StoreCategoriesVo>()

    private val _loginNavItem = MutableEventFlow<LoginNavItem>()
    val loginNavItem: EventFlow<LoginNavItem> get() = _loginNavItem

    init {
        getCategory()
    }

    private fun getCategory() {
        viewModelScope.launch(exceptionHandler) {
            platformStoreCategoryUseCase.getStoreCategories("BOSS_STORE").collect {
                if (it.code.toString() == "200") {
                    it.data?.let { categoriesDtoList ->
                        val categoriesVo = categoriesDtoList.map { storeCategoriesDto ->
                            storeCategoriesDto.dtoToVo()
                        }
                        categoryItemState.addAll(categoriesVo)
                    }
                }
            }
        }
    }

    fun signUp(bossName: String, storeName: String, businessNumber: String, certificationPhotoRequestBody: RequestBody?) {
        viewModelScope.launch(exceptionHandler) {
            if (certificationPhotoRequestBody != null) {
                val accessToken = (kakaoTokenProvider.refreshAccessToken() as? KakaoTokenResult.Success)?.accessToken
                if (accessToken == null) {
                    setErrorMessage(KAKAO_LOGIN_EXPIRED_MESSAGE)
                    return@launch
                }
                imageUploadUseCase.postImageUpload("BOSS_STORE_CERTIFICATION_IMAGE", certificationPhotoRequestBody).collect { it ->
                    if (it.code == "200") {
                        var businessNumberHyphen = ""
                        businessNumber.forEachIndexed { index, c ->
                            businessNumberHyphen += c
                            if (index == 2 || index == 4) {
                                businessNumberHyphen += "-"
                            }

                        }
                        authUseCase.signUp(
                            bossName = bossName,
                            businessNumber = businessNumberHyphen,
                            certificationPhotoUrl = it.data?.imageUrl.toString(),
                            socialType = "KAKAO",
                            storeCategoriesIds = selectedItems.map { selectedItem -> selectedItem.categoryId },
                            storeName = storeName,
                            token = accessToken
                        ).collect { result ->
                            val token = result.data?.token
                            if (!token.isNullOrEmpty()) {
                                val firebaseToken = async { FirebaseMessaging.getInstance().token.await() }
                                authUseCase.saveAccessToken(token).collect {
                                    bossDeviceUseCase.putBossDevice("FCM", firebaseToken.await()).collect {}
                                    _loginNavItem.emit(LoginNavItem.Waiting)
                                }
                            }
                            if (!result.errorMessage.isNullOrEmpty()) {
                                setErrorMessage(result.errorMessage.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    private companion object {
        const val KAKAO_LOGIN_EXPIRED_MESSAGE = "카카오 로그인 정보를 확인하지 못했어요. 다시 로그인해 주세요."
    }

    fun categorySelection(index: Int) {
        val item = categoryItemState[index]
        val isSelected = item.isSelected

        if (isSelected) {
            categoryItemState[index] = item.copy(isSelected = false)
            selectedItems.remove(item)
        } else if (selectedItems.size < 3) {
            categoryItemState[index] = item.copy(isSelected = true)
            selectedItems.add(categoryItemState[index])
        }
    }
}