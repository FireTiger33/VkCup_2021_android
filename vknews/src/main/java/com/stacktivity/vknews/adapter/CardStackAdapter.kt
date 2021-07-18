package com.stacktivity.vknews.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.stacktivity.vknews.adapter.fingerprints.NewsCardWithBackgroundImageFingerprint
import com.stacktivity.vknews.adapter.fingerprints.NewsCardWithImageFingerprint
import com.stacktivity.vknews.adapter.fingerprints.NewsCardWithTextFingerprint
import com.stacktivity.vknews.model.NewsItem


class CardStackAdapter :
    ListAdapter<NewsItem, NewsCardViewHolder<ViewBinding>>(NewsItemDiffCallback())
{
//    private var newsItems: List<NewsItem> = listOf()

    companion object {
        private val fingerprints = listOf(
            NewsCardWithImageFingerprint(),
            NewsCardWithBackgroundImageFingerprint(),
            NewsCardWithTextFingerprint()
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): NewsCardViewHolder<ViewBinding> {
        val inflater = LayoutInflater.from(parent.context)
        return fingerprints.find { it.getLayoutId() == viewType }
            ?.getViewHolder(inflater, parent)
            ?: throw IllegalArgumentException("View type not found: $viewType")
    }

    override fun onBindViewHolder(holder: NewsCardViewHolder<ViewBinding>, position: Int) {
//        val newsItem = newsItems[position]
        val newsItem = currentList[position]

        holder.onBind(newsItem)

        if (newsItem.sourceInfo.avatarUrl != null) {
            loadImage(holder.avatarContainer, newsItem.sourceInfo.avatarUrl, holder::setUserAvatar)
        }
        if (newsItem.imageUrl != null && holder is NewsCardWithImageViewHolder) {
            loadImage(holder.imageContainer, newsItem.imageUrl, holder::setImage)
        }
    }

    override fun getItemCount() = currentList.size

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return fingerprints.find { it.isRelativeItem(item) }
            ?.getLayoutId()
            ?: throw IllegalArgumentException("View type not found: $item")
    }

    private fun loadImage(view: View, url: String, imageSetter: (Drawable) -> Unit) {
        Glide.with(view)
            .load(url)
            .into(object : CustomViewTarget<View, Drawable?>(view) {
                override fun onLoadFailed(errorDrawable: Drawable?) {}

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    imageSetter(resource)
                }

                override fun onResourceCleared(placeholder: Drawable?) {}

            })
    }

}
