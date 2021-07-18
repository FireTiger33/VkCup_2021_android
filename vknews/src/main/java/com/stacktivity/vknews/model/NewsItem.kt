package com.stacktivity.vknews.model

import com.vk.sdk.api.newsfeed.dto.NewsfeedNewsfeedItem

data class NewsItem(
    val id: Int = counter++,
    val sourceInfo: NewsItemSourceInfo,
    val timePassed: String,
    val text: String = "",
    val imageUrl: String? = null,
    val imageWidth: Int? = null,
    val imageHeight: Int? = null,
    val numLikes: String = "",
    val numComments: String = "",
    val numReposts: String = "",
    val itemOrigin: NewsfeedNewsfeedItem? = null
) {
    companion object {
        private var counter = 0
    }
}
