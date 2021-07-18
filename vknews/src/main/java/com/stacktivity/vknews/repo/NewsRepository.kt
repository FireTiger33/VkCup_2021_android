package com.stacktivity.vknews.repo

import com.stacktivity.vknews.model.NewsItem

interface NewsRepository {

    suspend fun fetchNews(): List<NewsItem>

    fun getNews(): List<NewsItem>
    fun removeItem(item: NewsItem): List<NewsItem>
    fun removeItemAt(pos: Int): List<NewsItem>

    // Test mode
    fun getTestNews(): List<NewsItem>

    companion object {
        operator fun invoke(): NewsRepository {
            return NewsRepositoryImpl
        }
    }
}