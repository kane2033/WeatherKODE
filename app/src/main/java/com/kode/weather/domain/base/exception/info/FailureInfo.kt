package com.kode.weather.domain.base.exception.info


/**
 * Класс, хранящий информацию об ошибке.
 * @value text - текст ошибки.
 * @value buttonText - текст на кнопке. Если равен null, используется стандартная строка.
 * */
abstract class FailureInfo {
    abstract val text: String
    abstract val buttonText: String?
}