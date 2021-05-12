package com.kode.weather.presentation.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kode.weather.R
import com.kode.weather.domain.base.exception.Failure
import com.kode.weather.domain.base.exception.info.FailureInfo
import com.kode.weather.domain.base.exception.info.FullScreenFailureInfo
import com.kode.weather.domain.base.exception.info.SmallFailureInfo
import com.kode.weather.presentation.base.exception.FailureFragmentDirections

class BaseFragmentImpl : BaseFragment, Fragment() {

    override val viewModel: BaseViewModel = BaseViewModelImpl()

    // Отображение Toast уведомления со строкой из ресурсов
    override fun makeToast(@StringRes message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Отображение Toast уведомления с любой строкой
    override fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun makeSnackBar(
        @StringRes messageRes: Int,
        @StringRes actionTextRes: Int?,
        action: (() -> Unit)?
    ) {
        val message = getString(messageRes)
        val actionText = if (actionTextRes != null) getString(actionTextRes) else null
        makeSnackBar(message, actionText, action)
    }

    override fun makeSnackBar(
        message: String,
        actionText: String?,
        action: (() -> Unit)?
    ) {
        val view = view ?: return
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        action?.let { snackBar.setAction(actionText) { it() } }
        snackBar.show()
    }

    override fun navigateTo(
        @IdRes action: Int,
        args: Bundle?,
        navOptions: NavOptions?,
        extras: Navigator.Extras?
    ) {
        findNavController().navigate(action, args, navOptions, extras)
    }

    /**
     * Стандартный метод обработки ошибок, который дефолтно обрабатывает ошибку
     * отсутствия соединения [Failure.NetworkConnection] и, если не обработаны остальные ошибки,
     * отображает стандартное сообщение ошибки.
     *
     * В зависимости от возвращенного типа ([FullScreenFailureInfo] или [SmallFailureInfo])
     * открывается соответствующее окно, отображающее ошибку (полноэкранный диалог или снекбар соотв.)
     *
     * @param baseRetryClickedCallback - коллбэк, выполняемый при нажатии кнопки "повторить/обновить".
     * По стандарту ничего не делает.
     *
     * @param handleFailure - дополнительная обработка ошибок, присущая определенному экрану.
     * Стандартный параметр - отображение базового сообщения ошибки.
     * Для обработки доп. ошибок через данный параметр, необходимо создать функцию, в которой вернуть
     * объект [FailureInfo], содержащий сообщение ошибки и коллбек, производимый при получении
     * такой ошибки и нажатии кнопки "повторить/обновить".
     * */
    override fun handleFailure(
        baseRetryClickedCallback: () -> Unit,
        handleFailure: (failure: Failure) -> FailureInfo?
    ) {
        // Наблюдаем за изменением переменной ошибки
        viewModel.failure.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { failure ->

                // Получаем FailureInfo - текст ошибки и коллбек при обновлении
                val failureInfo: FailureInfo? = when (failure) {
                    is Failure.NetworkConnection -> getNetworkFailureInfo(baseRetryClickedCallback)
                    else -> handleFailure(failure)
                }

                // Открытие окна с ошибкой
                when (failureInfo) {
                    // Если инфа об ошибке не указана (null), показываем базовую в полном экране
                    null -> openFailureDialog(getBaseFailureInfo(baseRetryClickedCallback))
                    // Открываем полноэкранный диалог
                    is FullScreenFailureInfo -> openFailureDialog(failureInfo)
                    // Открываем снекбар
                    is SmallFailureInfo -> openFailureSnackBar(failureInfo)
                }
            }
        })
    }

    // Текст ошибки при проблемах с интернет-соединением
    private fun getNetworkFailureInfo(retryClicked: () -> Unit) = FullScreenFailureInfo(
        retryClicked,
        getString(R.string.error_network_connection_title),
        getString(R.string.error_network_connection)
    )

    // Базовый текст ошибки, если параметр handleFailure не указан
    private fun getBaseFailureInfo(baseRetryClickedCallback: () -> Unit) = FullScreenFailureInfo(
        baseRetryClickedCallback,
        getString(R.string.error_base_title),
        getString(R.string.error_base)
    )

    // Открытие полноэкранного диалогового фрагмента с ошибкой
    private fun openFailureDialog(failureInfo: FullScreenFailureInfo) {
        // Открываем фрагмент, передавая инфу об ошибке
        val action = FailureFragmentDirections.actionGlobalFailureFragment(failureInfo)
        findNavController().navigate(action)
    }

    // Открытие снэкбара с возможностью повторить операцию
    private fun openFailureSnackBar(failureInfo: SmallFailureInfo) {
        makeSnackBar(
            failureInfo.text,
            failureInfo.buttonText ?: getString(R.string.retry),
            failureInfo.retryClickedCallback
        )
    }
}