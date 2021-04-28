package com.kode.weather.presentation.weather

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.weather.R
import com.kode.weather.databinding.FragmentMapBinding
import com.kode.weather.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment: BaseFragment(R.layout.fragment_map) {

    override val viewModel: WeatherViewModel by viewModel()

    private val binding: FragmentMapBinding by viewBinding(FragmentMapBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@MapFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        handleFailure()
    }
}