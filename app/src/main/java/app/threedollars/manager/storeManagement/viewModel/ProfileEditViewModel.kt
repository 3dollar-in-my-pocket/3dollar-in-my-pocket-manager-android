package app.threedollars.manager.storeManagement.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.domain.usecase.BossStoreUseCase
import app.threedollars.domain.usecase.ImageUploadUseCase
import app.threedollars.domain.usecase.PlatformStoreCategoryUseCase
import app.threedollars.manager.util.dtoToVo
import app.threedollars.manager.vo.StoreCategoriesVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val platformStoreCategoryUseCase: PlatformStoreCategoryUseCase,
    private val bossStoreUseCase: BossStoreUseCase,
    private val imageUploadUseCase: ImageUploadUseCase
) : BaseViewModel() {

    init {
        getCategory()
    }

    val categoryItemState = mutableStateListOf<StoreCategoriesVo>()
    private val _selectedItems = MutableStateFlow<List<StoreCategoriesVo>>(listOf())
    val selectedItems: StateFlow<List<StoreCategoriesVo>> get() = _selectedItems

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

    fun patchBossStore(
        name: String? = null,
        sns: String? = null,
        imageRequestBody: RequestBody? = null,
        category: List<StoreCategoriesVo> = listOf()
    ) {

    }

    fun categorySelection(index: Int) {
        val item = categoryItemState[index]
        val isSelected = item.isSelected

        if (isSelected) {
            categoryItemState[index] = item.copy(isSelected = false)
            _selectedItems.value = categoryItemState.filter { !it.isSelected }
        } else if (_selectedItems.value.size < 3) {
            categoryItemState[index] = item.copy(isSelected = true)
            _selectedItems.value = categoryItemState.filter { it.isSelected }
        }
    }
}