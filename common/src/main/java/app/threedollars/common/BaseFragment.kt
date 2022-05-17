package app.threedollars.common

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.threedollars.common.ext.repeatOnCreated
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setScreenViewEvent()
        observeData()
    }

    private fun setScreenViewEvent() {
        FirebaseAnalytics.getInstance(requireContext()).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, this::class.java.simpleName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, this::class.java.simpleName)
        }
    }

    open fun observeData() {
        viewLifecycleOwner.repeatOnCreated {
            launch {
                viewModel.toastMsg.collect {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
            launch {
                viewModel.isLoading.collect {
                    initLoadingEvent(it)
                }
            }
        }
    }

    open fun initLoadingEvent(isLoading: Boolean) {
        // progress 이벤트 처리
    }
}