package com.kode.weather.presentation.base

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.squareup.picasso.Picasso


object BindingAdapters {

    @JvmStatic
    @BindingAdapter(value = ["imageUrl"])
    fun ImageView.setImageFromUrl(url: String?) {
        if (url != null && url.isNotBlank()) {
            //если есть картинка
            //устанавливаем картинку из url в imageView форматом Bitmap
            Picasso.get().load(url).into(this)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["loading"])
    fun ContentLoadingProgressBar.setLoading(isLoading: Boolean) {
        if (isLoading) this.show() else this.hide()
    }

    @JvmStatic
    @BindingAdapter(value = ["visibilityAnimated"])
    fun View.animateOnVisibilityChanged(visibility: Int) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.addTarget(this)
        TransitionManager.beginDelayedTransition(parent as ViewGroup, transition)
        this.visibility = visibility
    }
}
