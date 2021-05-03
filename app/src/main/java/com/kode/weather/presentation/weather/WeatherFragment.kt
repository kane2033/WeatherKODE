package com.kode.weather.presentation.weather

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.weather.R
import com.kode.weather.databinding.FragmentWeatherBinding
import com.kode.weather.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WeatherFragment : BaseFragment(R.layout.fragment_weather) {

    private val args: WeatherFragmentArgs by navArgs()

    override val viewModel: WeatherViewModel by viewModel { parametersOf(args.cityName) }

    private val binding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@WeatherFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        handleFailure(handleRequestFailure = { failure ->
            when (failure.code) {
                404 -> makeToast(R.string.error_city_not_found)
            }
        })
    }
}