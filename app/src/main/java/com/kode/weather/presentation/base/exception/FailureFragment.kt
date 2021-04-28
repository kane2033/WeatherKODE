package com.kode.weather.presentation.base.exception

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.weather.R
import com.kode.weather.databinding.FragmentFailureBinding
import org.koin.androidx.viewmodel.ext.android.stateViewModel

/**
 * Диалоговый фрагмент, ответственный за отображение информации об ошибке
 * и предоставление повтора операции, из-за которой возникла данная ошибка.
 * */
class FailureFragment : DialogFragment(R.layout.fragment_failure) {

    private val viewModel: FailureViewModel by stateViewModel()

    private val binding: FragmentFailureBinding by viewBinding(FragmentFailureBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Отображаем диалог в фуллскрине
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_DialogWhenLarge)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@FailureFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        binding.retryButton.setOnClickListener {
            viewModel.failureInfo.value?.retryClickedCallback?.invoke()
            dismissAllowingStateLoss()
        }
    }
}