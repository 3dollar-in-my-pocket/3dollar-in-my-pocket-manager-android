package app.threedollars.manager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        Navigation.setViewNavController(navView, navController)
    }

    private fun tryKakaoLogin() {
        val loginResCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("localClassName", "로그인 실패", error)
                // showToast(R.string.error_no_kakao_login)
            } else if (token != null) {
                Log.d("localClassName", token.toString())
                // tryLoginBySocialType(token)
            }
        }

        if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
            LoginClient.instance.loginWithKakaoTalk(this, callback = loginResCallback)
        } else {
            LoginClient.instance.loginWithKakaoAccount(
                this,
                callback = loginResCallback
            )
        }
    }
}