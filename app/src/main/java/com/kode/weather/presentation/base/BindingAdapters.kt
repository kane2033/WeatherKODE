package com.kode.weather.presentation.base

import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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
}