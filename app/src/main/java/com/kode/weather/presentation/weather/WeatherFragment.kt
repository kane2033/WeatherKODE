package com.kode.weather.presentation.weather

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.weather.R
import com.kode.weather.databinding.FragmentWeatherBinding
import com.kode.weather.domain.base.exception.info.FullScreenFailureInfo
import com.kode.weather.domain.weather.exception.FetchWeatherFailure
import com.kode.weather.presentation.base.*
import com.kode.weather.presentation.base.exception.RetryClickedInterface
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WeatherFragment : Fragment(R.layout.fragment_weather), RetryClickedInterface {

    private val args: WeatherFragmentArgs by navArgs()

    private val viewModel: WeatherViewModel by viewModel { parametersOf(args.cityName) }

    private val binding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@WeatherFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.uiState.observeEvent(viewLifecycleOwner, {
            if (it is UiState.Failure) {
                openFailureView(failure = it.failure, getFailureInfo = { failure ->
                    when (failure) {
                        is FetchWeatherFailure.NotFound -> FullScreenFailureInfo(
                            title = getString(R.string.error_city_not_found_title),
                            text = getString(R.string.error_city_not_found),
                            buttonText = getString(R.string.error_city_not_found_button)
                        )
                        else -> null
                    }
                })
            }
        })

        setHasOptionsMenu(true)
    }

    override fun onRetryClicked() {
        val failureState = viewModel.uiState.value?.peekContent() as? UiState.Failure
        when (failureState?.failure) {
            is FetchWeatherFailure.NotFound -> {
                findNavController().popBackStack(R.id.weatherFragment, true)
            }
            else -> viewModel.fetchCityWeather()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_weather, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.shareButton) {
            makeToast(R.string.in_development)
        }
        return super.onOptionsItemSelected(item)
    }
}