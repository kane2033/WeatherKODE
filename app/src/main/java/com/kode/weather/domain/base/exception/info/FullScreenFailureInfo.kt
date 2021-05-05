package com.kode.weather.domain.base.exception.info

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Класс, хранящий информацию, отображаемую на полном экране (FailureFragment).
 * @param retryClickedCallback - действие при нажатии кнопки "обновить/повторить".
 * @param title - заголовок экрана ошибки.
 * @param text - подробный текст ошибки.
 * @param buttonText - текст на кнопке. Если не указан (равен null), используется стандартная строка.
 * */
@Parcelize
data class FullScreenFailureInfo(
    override val retryClickedCallback: () -> Unit,
    val title: String,
    override val text: String,
    override val buttonText: String? = null
) : Parcelable, FailureInfo()