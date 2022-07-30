package app.threedollars.manager.sign.viewmodel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.entity.user.NewUser
import app.threedollars.domain.repository.SignRepository
import app.threedollars.manager.util.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: SignRepository
) : BaseViewModel() {

    private val _signUpResult = MutableEventFlow<Boolean>()
    val signUpResult: EventFlow<Boolean>
        get() = _signUpResult

    fun uploadImage() {
        getImageFiles()
    }

    private fun getImageFiles(): MultipartBody.Part? {
        FileUtils.drawableToFile()?.run {
            val requestFile = asRequestBody("image/*".toMediaType())
            return MultipartBody.Part.createFormData("images", name, requestFile)
        }
        return null
    }

    fun signUp(newUser: NewUser) {
        if (!checkNewUser(newUser)) {
            return
        }

        viewModelScope.launch(exceptionHandler) {
            repository.signUpToManagerServer(
                kakaoAccessToken = repository.getKakaoAccessToken().firstOrNull() ?: "",
                newUser
            )
        }
    }

    private fun checkNewUser(newUser: NewUser): Boolean {
        return !newUser.bossName.isNullOrBlank()
                && !newUser.storeName.isNullOrBlank()
                && newUser.businessNumber?.length == 10
                && newUser.businessNumber?.all { it.isDigit() } == true
                && !newUser.certificationPhotoUrl.isNullOrBlank()
                && newUser.contactsNumber?.length == 11
                && newUser.contactsNumber?.all { it.isDigit() } == true
    }
}