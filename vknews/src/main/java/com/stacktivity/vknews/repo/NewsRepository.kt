package com.stacktivity.vknews.repo

import com.stacktivity.vknews.model.NewsItem

interface NewsRepository {

    /**
     * Used to get a new batch of news
     *
     * @return news with fetched items
     */
    suspend fun fetchNews(): List<NewsItem>

    fun getNews(): List<NewsItem>
    fun clearNews()

    /**
     * Remove an [item] from news list
     *
     * @return updated news list
     */
    fun removeItem(item: NewsItem): List<NewsItem>

    /**
     * Remove an item at [pos] from news list
     *
     * @return updated news list
     */
    fun removeItemAt(pos: Int): List<NewsItem>

    companion object {
        operator fun invoke(): NewsRepository {
            return NewsRepositoryImpl
        }
    }
}