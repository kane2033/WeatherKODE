package com.kode.weather.presentation.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.kode.weather.domain.base.exception.Failure
import com.kode.weather.domain.base.exception.info.FailureInfo

/**
 * Базовый класс [Fragment],
 * имеющий общие для других фрагментов методы.
 * */
interface BaseFragment {

    val viewModel: BaseViewModel

    // Отображение Toast уведомления со строкой из ресурсов
    fun makeToast(@StringRes message: Int)

    // Отображение Toast уведомления с любой строкой
    fun makeToast(message: String)

    fun makeSnackBar(
        @StringRes messageRes: Int,
        @StringRes actionTextRes: Int? = null,
        action: (() -> Unit)? = null
    )

    fun makeSnackBar(
        message: String,
        actionText: String? = null,
        action: (() -> Unit)? = null
    )

    fun navigateTo(
        @IdRes action: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
        extras: Navigator.Extras? = null
    )

    fun handleFailure(
        baseRetryClickedCallback: () -> Unit = {},
        handleFailure: (failure: Failure) -> FailureInfo? = { null },
        handleRequestFailure: (code: Int) -> FailureInfo? = { null }
    )
}