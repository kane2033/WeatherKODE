package com.kode.weather.presentation.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.kode.weather.R
import com.kode.weather.domain.base.exception.Failure

/**
 * Базовый класс [Fragment],
 * имеющий общие для других фрагментов методы.
 * */
abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    protected abstract val viewModel: BaseViewModel

    // Отображение Toast уведомления со строкой из ресурсов
    internal fun makeToast(@StringRes message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Отображение Toast уведомления с любой строкой
    internal fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun navigateTo(
        @IdRes action: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
        extras: Navigator.Extras? = null
    ) {
        findNavController().navigate(action, args, navOptions, extras)
    }

    protected fun handleFailure(
        handleFailure: (failure: Failure) -> Unit = { makeToast(R.string.error_base_title) },
        handleRequestFailure: (failure: Failure) -> Unit = {}
    ) {
        viewModel.failure.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { failure ->
                when (failure) {
                    is Failure.NetworkConnection -> makeToast(R.string.error_network_connection_title)
                    is Failure.MissingContentFailure -> makeToast(R.string.error_missing_content_title)
                    is Failure.RequestFailure -> handleRequestFailure(failure)
                    else -> handleFailure(failure)
                }
            }
        })
    }
}