package com.kode.weather.presentation.weather

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.navArgs
import com.kode.weather.R
import com.kode.weather.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WeatherFragment : BaseFragment(R.layout.fragment_weather) {

    private val args: WeatherFragmentArgs by navArgs()

    override val viewModel: WeatherViewModel by viewModel { parametersOf(args.cityName) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.city.observe(viewLifecycleOwner, {
            Log.e("CITY", "city: $it")
        })
    }
}