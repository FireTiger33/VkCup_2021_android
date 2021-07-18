package com.stacktivity.vknews.adapter.fingerprints

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stacktivity.vknews.R.layout.news_card_with_background_image
import com.stacktivity.vknews.adapter.NewsCardViewHolder
import com.stacktivity.vknews.adapter.NewsCardWithImageViewHolder
import com.stacktivity.vknews.adapter.NewsItemFingerprint
import com.stacktivity.vknews.databinding.NewsCardWithBackgroundImageBinding
import com.stacktivity.vknews.model.NewsItem

/**
 * Fingerprint ViewHolder with text and vertical image
 *
 * @see NewsCardWithTextFingerprint
 * @see NewsCardWithImageFingerprint
 */
class NewsCardWithBackgroundImageFingerprint : NewsItemFingerprint<NewsCardWithBackgroundImageBinding> {
    override fun isRelativeItem(item: NewsItem): Boolean {
        return if (item.imageWidth == null || item.imageHeight == null) {
            false
        } else item.imageWidth <= item.imageHeight
    }

    override fun getLayoutId() = news_card_with_background_image

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): NewsCardViewHolder<NewsCardWithBackgroundImageBinding> {
        return CardWithBackgroundImageViewHolder(
            NewsCardWithBackgroundImageBinding.inflate(layoutInflater, parent, false)
        )
    }
}

class CardWithBackgroundImageViewHolder(
    binding: NewsCardWithBackgroundImageBinding
) : NewsCardWithImageViewHolder<NewsCardWithBackgroundImageBinding>(binding) {

    override val imageContainer: View get() = binding.image
    override val avatarContainer: View get() = binding.info.avatar

    override fun onBind(item: NewsItem) {
        reset()
        binding.apply {
            info.username.text = item.sourceInfo.name
            info.timePassed.text = item.timePassed
            text.text = item.text
            bottomTextFix(text)
            socialActions.likesCount.text = item.numLikes
            socialActions.commentsCount.text = item.numComments
            socialActions.repostsCount.text = item.numReposts
        }
    }

    private fun reset() {
        binding.image.setImageDrawable(null)
        binding.text.gravity = Gravity.BOTTOM
    }

    override fun setImage(drawable: Drawable) {
        binding.image.setImageDrawable(drawable)
    }

    override fun setUserAvatar(drawable: Drawable) {
        binding.info.avatar.setImageDrawable(drawable)
    }
}