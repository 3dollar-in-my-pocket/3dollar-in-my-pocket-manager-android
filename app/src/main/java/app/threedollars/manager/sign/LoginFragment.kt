package app.threedollars.manager.sign

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import app.threedollars.common.BaseFragment
import app.threedollars.manager.sign.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}