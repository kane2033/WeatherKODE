package com.kode.weather.domain.base.exception.info

/**
 * Аналогично классу [FullScreenFailureInfo], хранит информацию об ошибке
 * и коллбек для повтора операциии, но используется в неполноэкранном окне ошибки (SnackBar).
 * */
data class SmallFailureInfo(
    override val retryClickedCallback: () -> Unit,
    override val text: String,
    override val buttonText: String? = null
) : FailureInfo()