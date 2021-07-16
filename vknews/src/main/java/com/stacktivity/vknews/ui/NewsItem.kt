package com.stacktivity.vknews.ui

data class NewsItem(
        val id: Long = counter++,
        val name: String,
        val timePassed: String,
        val imageUrl: String
) {
    companion object {
        private var counter = 0L
    }
}
