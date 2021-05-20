package com.kode.weather.presentation.base

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kode.weather.R
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.base.exception.Failure
import com.kode.weather.domain.base.exception.info.FailureInfo
import com.kode.weather.domain.base.exception.info.FullScreenFailureInfo
import com.kode.weather.domain.base.exception.info.SmallFailureInfo
import com.kode.weather.presentation.base.exception.FailureFragmentDirections

// Отображение Toast уведомления со строкой из ресурсов
fun Fragment.makeToast(@StringRes message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// Отображение Toast уведомления с любой строкой
fun Fragment.makeToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.makeSnackBar(
    @StringRes messageRes: Int,
    @StringRes actionTextRes: Int?,
    action: (() -> Unit)?
) {
    val message = getString(messageRes)
    val actionText = if (actionTextRes != null) getString(actionTextRes) else null
    makeSnackBar(message, actionText, action)
}

fun Fragment.makeSnackBar(
    message: String,
    actionText: String?,
    action: (() -> Unit)?
) {
    val view = view ?: return
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    action?.let { snackBar.setAction(actionText) { it() } }
    snackBar.show()
}

/**
 * Освобождает от необходимости писать getContentIfNotHandled для [LiveData] с типом [Event]:
 * @param [observer] выполнится, только если ивент еще не обработан.
 * */
//
fun <T> LiveData<Event<T>>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    observer: (value: T) -> Unit
) {
    observe(lifecycleOwner, { event ->
        event.getContentIfNotHandled()?.let { content -> observer(content) }
    })
}

/**
 * Функция открывает окно ошибки в зависимости от переданного параметра [failure].
 * По стандарту, обрабатывает все [Failure.NetworkFailure] и, если переданный параметр [failure]
 * не обработан, отображает стандартное сообщение ошибки.
 *
 * В зависимости от возвращенного типа ([FullScreenFailureInfo] или [SmallFailureInfo])
 * открывается соответствующее окно, отображающее ошибку (полноэкранный диалог или снекбар соотв.)
 *
 * @param failure - ошибка, на основне которой открывается соответствующее окно ошибки.
 * @param getFailureInfo - функция, которая должна
 * вернуть соответсвующий [FailureInfo] указанному [Failure].
 * Для обработки доп. ошибок через данный параметр, необходимо создать функцию, в которой вернуть
 * объект [FailureInfo], содержащий сообщение ошибки и коллбек, производимый при получении
 * такой ошибки и нажатии кнопки "повторить/обновить".
 * */
fun Fragment.openFailureView(
    failure: Failure,
    getFailureInfo: (failure: Failure) -> FailureInfo? = { null }
) {
    // Получаем FailureInfo - текст ошибки и коллбек при обновлении
    val failureInfo: FailureInfo? = when (failure) {
        is Failure.NetworkFailure -> handleNetworkFailure(failure)
        else -> getFailureInfo(failure)
    }

    // Открытие окна с ошибкой
    when (failureInfo) {
        // Открываем полноэкранный диалог
        is FullScreenFailureInfo -> openFailureDialog(failureInfo)
        // Открываем снекбар
        is SmallFailureInfo -> openFailureSnackBar(failureInfo)
        // Если инфа об ошибке не указана (null), показываем базовую в полном экране
        null -> FullScreenFailureInfo(
            getString(R.string.error_base_title),
            getString(R.string.error_base)
        )
    }
}

// Открытие полноэкранного диалогового фрагмента с ошибкой
private fun Fragment.openFailureDialog(failureInfo: FullScreenFailureInfo) {
    // Открываем фрагмент, передавая инфу об ошибке
    val action = FailureFragmentDirections.actionGlobalFailureFragment(failureInfo)
    findNavController().navigate(action)
}

// Открытие снэкбара с возможностью повторить операцию
private fun Fragment.openFailureSnackBar(failureInfo: SmallFailureInfo) {
    makeSnackBar(
        failureInfo.text,
        failureInfo.buttonText ?: getString(R.string.retry),
        failureInfo.retryClickedCallback
    )
}

private fun Fragment.handleNetworkFailure(networkFailure: Failure.NetworkFailure): FailureInfo? {
    return when (networkFailure) {
        is Failure.NoInternet -> FullScreenFailureInfo(
            getString(R.string.error_no_internet_title),
            getString(R.string.error_no_internet)
        )
        is Failure.ServerFailure -> FullScreenFailureInfo(
            getString(R.string.error_server_title),
            getString(R.string.error_server)
        )
        is Failure.Timeout -> FullScreenFailureInfo(
            getString(R.string.error_timeout_title),
            getString(R.string.error_timeout)
        )
        else -> null
    }
}