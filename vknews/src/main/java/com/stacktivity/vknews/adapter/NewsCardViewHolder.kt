package com.stacktivity.vknews.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.stacktivity.vknews.model.NewsItem


abstract class NewsCardViewHolder<out V : ViewBinding>(
    protected val binding: V
) : RecyclerView.ViewHolder(binding.root) {
    abstract val avatarContainer: View

    abstract fun onBind(item: NewsItem)

    abstract fun setUserAvatar(drawable: Drawable)

    /**
     * It is used for more correct display of the text when it has [Gravity.BOTTOM]
     * and does not fit in the view
     *
     * Set gravity of [TextView] content to [Gravity.TOP].
     */
    fun bottomTextFix(textView: TextView) {
        if (textView.text.isEmpty()) return

        textView.post {
            val maxLines = textView.height / textView.lineHeight - 1
            textView.maxLines = if (maxLines > 1) maxLines else 1
        }
    }

//    abstract fun onViewDetached()
}