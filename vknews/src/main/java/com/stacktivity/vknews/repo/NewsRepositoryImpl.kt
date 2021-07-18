package com.stacktivity.vknews.repo

import android.util.Log
import com.stacktivity.vknews.model.NewsItem
import com.stacktivity.vknews.model.NewsItemSourceInfo
import com.stacktivity.vknews.utils.format
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.groups.dto.GroupsGroupFull
import com.vk.sdk.api.newsfeed.NewsfeedService
import com.vk.sdk.api.newsfeed.dto.NewsfeedFilters
import com.vk.sdk.api.newsfeed.dto.NewsfeedGetResponse
import com.vk.sdk.api.newsfeed.dto.NewsfeedNewsfeedItem
import com.vk.sdk.api.users.dto.UsersUserFull
import com.vk.sdk.api.wall.dto.WallWallpostAttachmentType
import kotlinx.coroutines.suspendCancellableCoroutine
import org.ocpsoft.prettytime.PrettyTime
import java.util.Locale
import kotlin.coroutines.resume

internal object NewsRepositoryImpl : NewsRepository {

    private val tag: String = NewsRepositoryImpl::class.java.simpleName

    private val newsFilter = listOf(
        NewsfeedFilters.POST,
//        NewsfeedFilters.NOTE,
//        NewsfeedFilters.WALL_PHOTO,
    )

    private val news: MutableList<NewsItem> = mutableListOf()

    private var newsNextFrom: String? = null


    /**
     * Uses VK API newsfeed.get method to get a news posts
     *
     * @return news with fetched items
     */
    override suspend fun fetchNews(): List<NewsItem> = suspendCancellableCoroutine { cont ->
        val callback = object : VKApiCallback<NewsfeedGetResponse> {
            override fun fail(error: Exception) {
                Log.e(tag, "${error.message}")
                error.printStackTrace()
            }

            override fun success(result: NewsfeedGetResponse) {
                if (cont.isCancelled) {
                    return
                }

                var newsfeeds: List<NewsItem> = result.items?.map { item ->
                    when (item) {
                        is NewsfeedNewsfeedItem.NewsfeedItemWallpost -> {
                            val userInfo =
                                getUserInfo(item.sourceId, result.profiles, result.groups)
                            item.mapToNewsItem(userInfo ?: MockNewsRepo.getSourceInfo())
                        }

                        else -> throw IllegalArgumentException("Unsupported post type: $item")
                    }
                } ?: listOf()
                newsfeeds = newsfeeds.filter { it.text.isNotEmpty() || it.imageUrl != null }

                newsNextFrom = result.nextFrom

                news.addAll(newsfeeds)
                cont.resume(news.toList())
            }

        }

        VK.execute(NewsfeedService().newsfeedGet(newsFilter, startFrom = newsNextFrom), callback)
    }

    override fun getNews(): List<NewsItem> {
        return news.toList()
    }

    override fun clearNews() {
        news.clear()
    }

    /**
     * Remove an [item] from news list
     *
     * @return updated news list
     */
    override fun removeItem(item: NewsItem): List<NewsItem> {
        return news.apply { remove(item) }.toList()
    }

    /**
     * Remove an item at [pos] from news list
     *
     * @return updated news list
     */
    override fun removeItemAt(pos: Int): List<NewsItem> {
        return news.apply { removeAt(pos) }.toList()
    }

    private fun getUserInfo(
        sourceId: Int?, profiles: List<UsersUserFull>?, groups: List<GroupsGroupFull>?
    ): NewsItemSourceInfo? {
        var sourceInfo: NewsItemSourceInfo? = null
        if (sourceId == null) return null
        if (sourceId < 0) {
            groups?.find { it.id == -sourceId }?.let { group ->
                sourceInfo = NewsItemSourceInfo(
                    group.name ?: "",
                    group.photo50 ?: group.photo100 ?: group.photo200
                )
            }
        } else {
            profiles?.find { it.id == sourceId }?.let { user ->
                sourceInfo = NewsItemSourceInfo(
                    user.nickname ?: "",
                    user.photo50 ?: user.photo100 ?: user.photo200
                )
            }
        }

        return sourceInfo
    }

    private fun NewsfeedNewsfeedItem.NewsfeedItemWallpost.mapToNewsItem(sourceInfo: NewsItemSourceInfo): NewsItem {
        val pretty = PrettyTime(Locale.getDefault())
        val images = attachments
            ?.find { it.type == WallWallpostAttachmentType.PHOTO }
            ?.photo?.sizes

        val image = images?.last()

        return NewsItem(
            id = postId ?: 0,
            sourceInfo = sourceInfo,
            timePassed = pretty.format(date ?: 0),
            text = text ?: "",
            imageUrl = image?.url,
            imageWidth = image?.width,
            imageHeight = image?.height,
            numLikes = numberToString(likes?.userLikes),
            numComments = numberToString(comments?.count),
            numReposts = numberToString(reposts?.count),
            itemOrigin = this
        )
    }

    private fun numberToString(num: Int?) = if (num != null && num > 0) num.toString() else ""
}