package com.stacktivity.vknews.adapter.fingerprints


import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stacktivity.vknews.R.layout.news_card_with_text
import com.stacktivity.vknews.adapter.NewsCardViewHolder
import com.stacktivity.vknews.adapter.NewsItemFingerprint
import com.stacktivity.vknews.databinding.NewsCardWithTextBinding
import com.stacktivity.vknews.model.NewsItem


/**
 * Fingerprint ViewHolder with text
 *
 * @see NewsCardWithImageFingerprint
 * @see NewsCardWithBackgroundImageFingerprint
 */
class NewsCardWithTextFingerprint : NewsItemFingerprint<NewsCardWithTextBinding> {
    override fun isRelativeItem(item: NewsItem): Boolean {
        return item.imageUrl == null
    }

    override fun getLayoutId() = news_card_with_text

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): NewsCardViewHolder<NewsCardWithTextBinding> {
        return CardWithTextViewHolder(
            NewsCardWithTextBinding.inflate(layoutInflater, parent, false)
        )
    }
}

class CardWithTextViewHolder(
    binding: NewsCardWithTextBinding
) : NewsCardViewHolder<NewsCardWithTextBinding>(binding) {

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

    override fun setUserAvatar(drawable: Drawable) {
        binding.info.avatar.setImageDrawable(drawable)
    }

    private fun reset() {
        binding.text.gravity = Gravity.BOTTOM
    }
}