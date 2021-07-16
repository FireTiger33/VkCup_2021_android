package com.stacktivity.vknews.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.stacktivity.vknews.databinding.NewsCardWithBackgroundImageBinding


class CardStackAdapter(
    private var newsItems: List<NewsItem> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(NewsCardWithBackgroundImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsItems[position]
        val name = "${newsItem.id}. ${newsItem.name}"

        holder.name.text = name
        holder.timePassed.text = newsItem.timePassed

        loadImage(holder.imageContainer, newsItem.imageUrl, holder.imageSetter)
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    fun setSpots(newsItems: List<NewsItem>) {
        this.newsItems = newsItems
    }

    fun getSpots(): List<NewsItem> {
        return newsItems
    }

    private fun loadImage(imageContainer: View, url: String, imageSetter: (Drawable) -> Unit) {
        Glide.with(imageContainer)
            .load(url)
            .into(object : CustomViewTarget<View, Drawable?>(imageContainer) {
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

    class ViewHolder(
        binding: NewsCardWithBackgroundImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.name
        val timePassed = binding.timePassed
        val imageContainer = binding.imageBackgroundContainer
        val imageSetter = binding.imageBackgroundContainer::setBackground
    }

}
