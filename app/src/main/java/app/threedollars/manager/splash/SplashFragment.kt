package app.threedollars.manager.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.threedollars.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SplashScreen()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(lifecycleScope) {
            launch {
                viewModel.isLogin.collect {
                    Toast.makeText(requireContext(), "로그인 되어있는 유저입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            launch {
                viewModel.needSignUp.collect {

                }
            }
        }
    }
}