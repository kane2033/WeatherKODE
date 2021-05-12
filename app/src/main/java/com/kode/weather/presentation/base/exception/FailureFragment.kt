package com.kode.weather.presentation.base.exception

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.weather.R
import com.kode.weather.databinding.FragmentFailureBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Диалоговый фрагмент, ответственный за отображение информации об ошибке
 * и предоставление повтора операции, из-за которой возникла данная ошибка.
 * */
class FailureFragment : DialogFragment(R.layout.fragment_failure) {

    private val args: FailureFragmentArgs by navArgs()

    private val viewModel: FailureViewModel by viewModel { parametersOf(args.failureInfo) }

    private val binding: FragmentFailureBinding by viewBinding(FragmentFailureBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Отображаем диалог в фуллскрине
        setStyle(STYLE_NO_TITLE, R.style.Theme_AppCompat_Light_DialogWhenLarge)
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