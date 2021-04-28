package com.kode.weather.domain.base.exception

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Класс, хранящий информацию, отображаемую на экране ошибки (FailureFragment).
 * @param retryClickedCallback - действие при нажатии кнопки "обновить/повторить".
 * @param title - заголовок экрана ошибки.
 * @param text - подробный текст ошибки.
 * @param buttonText - текст на кнопке. Если не указан (равен null), используется стандартная строка.
 * */
@Parcelize
data class FailureInfo(
    val retryClickedCallback: () -> Unit,
    val title: String,
    val text: String,
    val buttonText: String? = null
) : Parcelable {
}