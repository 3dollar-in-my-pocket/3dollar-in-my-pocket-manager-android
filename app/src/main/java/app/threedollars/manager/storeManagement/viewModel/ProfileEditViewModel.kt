package app.threedollars.manager.storeManagement.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import app.threedollars.domain.usecase.PlatformStoreCategoryUseCase
import app.threedollars.manager.util.dtoToVo
import app.threedollars.manager.vo.BossStoreRetrieveVo
import app.threedollars.manager.vo.StoreCategoriesVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val platformStoreCategoryUseCase: PlatformStoreCategoryUseCase,
) : BaseViewModel() {

    init {
        getCategory()
    }

    val categoryItemState = mutableStateListOf<StoreCategoriesVo>()
    private val selectedItems = mutableListOf<StoreCategoriesVo>()

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