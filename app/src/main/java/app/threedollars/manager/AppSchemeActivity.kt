package app.threedollars.manager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import app.threedollars.common.REVIEW_LIST
import app.threedollars.manager.MainActivity.Companion.SCREEN_TYPE_KEY
import app.threedollars.manager.sign.LoginNavItem
import app.threedollars.manager.sign.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppSchemeActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel.checkMyInfo()
        lifecycleScope.launch {
            loginViewModel.loginNavItem.collect {
                when (it) {
                    LoginNavItem.Home -> handleAppSchemeIntent(intent)
                    else -> startActivity(Intent(this@AppSchemeActivity, LoginActivity::class.java))
                }
            }
        }
    }

    private fun handleAppSchemeIntent(intent: Intent?) {
        intent?.data?.let { uri ->
            handleScheme(uri)
        } ?: run {
            finish()
        }
    }

    private fun handleScheme(uri: Uri) {
        when (uri.lastPathSegment ?: uri.host ?: "") {
            REVIEW_LIST -> {
                startActivity(Intent(this, MainActivity::class.java).apply {
                    putExtra(SCREEN_TYPE_KEY, REVIEW_LIST)
                })
            }
        }
        finish()
    }
}
