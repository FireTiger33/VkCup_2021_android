package com.stacktivity.vknews.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.stacktivity.vknews.model.NewsItem

interface NewsItemFingerprint<V : ViewBinding> {

    fun isRelativeItem(item: NewsItem): Boolean

    @LayoutRes
    fun getLayoutId(): Int

    fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): NewsCardViewHolder<V>

}