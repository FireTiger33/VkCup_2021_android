package com.stacktivity.vknews.adapter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.viewbinding.ViewBinding

abstract class NewsCardWithImageViewHolder<out T : ViewBinding>(
    binding: T,
) : NewsCardViewHolder<T>(binding) {
    abstract val imageContainer: View

    abstract fun setImage(drawable: Drawable)
}