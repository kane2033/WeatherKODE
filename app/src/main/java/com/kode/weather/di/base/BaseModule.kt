package com.kode.weather.di.base

import com.kode.weather.domain.base.exception.info.FullScreenFailureInfo
import com.kode.weather.presentation.base.exception.FailureViewModel
import com.kode.weather.presentation.base.viewmodel.BaseViewModel
import com.kode.weather.presentation.base.viewmodel.BaseViewModelImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object BaseModule {
    val module = module {
        viewModel { (failureInfo: FullScreenFailureInfo) -> FailureViewModel(failureInfo, get()) }
        factory<BaseViewModel> { BaseViewModelImpl() }
    }
}