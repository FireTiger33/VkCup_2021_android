package com.stacktivity.vknews.adapter.fingerprints

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stacktivity.vknews.R.layout.news_card_with_image
import com.stacktivity.vknews.adapter.NewsCardViewHolder
import com.stacktivity.vknews.adapter.NewsCardWithImageViewHolder
import com.stacktivity.vknews.adapter.NewsItemFingerprint
import com.stacktivity.vknews.databinding.NewsCardWithImageBinding
import com.stacktivity.vknews.model.NewsItem


/**
 * Fingerprint ViewHolder with text and horizontal image
 *
 * @see NewsCardWithTextFingerprint
 * @see NewsCardWithBackgroundImageFingerprint
 */
class NewsCardWithImageFingerprint : NewsItemFingerprint<NewsCardWithImageBinding> {
    override fun isRelativeItem(item: NewsItem): Boolean {
        return if (item.imageWidth == null || item.imageHeight == null) {
            false
        } else item.imageWidth > item.imageHeight
    }

    override fun getLayoutId() = news_card_with_image

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): NewsCardViewHolder<NewsCardWithImageBinding> {
        return CardWithImageViewHolder(
            NewsCardWithImageBinding.inflate(layoutInflater, parent, false)
        )
    }
}

class CardWithImageViewHolder(
    binding: NewsCardWithImageBinding
) : NewsCardWithImageViewHolder<NewsCardWithImageBinding>(binding) {

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
        val size = drawable.bounds
        binding.image.post {
            if(binding.text.text.isEmpty()) {
                resizeImageViewIfNeed(size.width(), size.height())
            }
        }

        binding.image.setImageDrawable(drawable)
    }

    override fun setUserAvatar(drawable: Drawable) {
        binding.info.avatar.setImageDrawable(drawable)
    }

    private fun resizeImageViewIfNeed(imageWidth: Int, imageHeight: Int) {
        val calcHeight = calculateViewHeight(binding.image.width, imageWidth, imageHeight)
        binding.image.layoutParams.height = calcHeight
    }

    private fun calculateViewHeight(viewWidth: Int, contentWidth: Int, contentHeight: Int): Int {
        val cropFactor: Double = viewWidth / contentWidth.toDouble()
        return (cropFactor * contentHeight).toInt()
    }
}