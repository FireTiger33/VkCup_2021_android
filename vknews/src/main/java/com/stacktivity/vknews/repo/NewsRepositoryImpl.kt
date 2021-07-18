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

                var newsfeeds: List<NewsItem> = result.items?.mapIndexed { i, item ->
                    when (item) {
                        is NewsfeedNewsfeedItem.NewsfeedItemWallpost -> {
                            val userInfo =
                                getUserInfo(item.sourceId, result.profiles, result.groups)
                            item.mapToNewsItem(userInfo ?: getTestSourceInfo())
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

    override fun getTestNews() = news.apply {
        addAll(
            arrayListOf(
                NewsItem(
                    sourceInfo = getTestSourceInfo(),
                    timePassed = "час назад",
                    imageUrl = "https://source.unsplash.com/Xq1ntWruZQI/600x800",
                    imageWidth = 600, imageHeight = 800,
                ),
                NewsItem(
                    sourceInfo = getTestSourceInfo(),
                    timePassed = "три часа назад",
                    imageUrl = "https://source.unsplash.com/NYyCqdBOKwc/600x800",
                    imageWidth = 600, imageHeight = 800,
                    text = "Kyoto: Fushimi Inari Shrine"
                ),
                NewsItem(
                    sourceInfo = getTestSourceInfo(),
                    timePassed = "сегодня в 8:30",
                    imageUrl = "https://1.bp.blogspot.com/-M5yfX86zJeE/Xclwb6S0IwI/AAAAAAAApUg/" +
                        "bsf2qwYifPckCQd-Lm5Si12kDOoREyJVQCLcBGAsYHQ/s1600/francisco-negroni-7.jpeg",
                    imageWidth = 800, imageHeight = 531,
                    text = "Фотограф когда-то изучал рекламную фотографию и туризм, " +
                        "но как только впервые стал свидетелем извержения вулкана," +
                        " понял, что в своей карьере фотографа хочет фокусироваться именно" +
                        " на этом впечатляющем природном явлении."
                ),
                NewsItem(
                    sourceInfo = getTestSourceInfo(),
                    timePassed = "три часа назад",
                    text = "«В жарких районах типа Персидского залива летом температура в некоторых " +
                        "его помещениях может доходить до +70 °C." +
                        " Постоянная возня с механизмами, руки, пропахшие маслом и мазутом." +
                        " Не такую картину рисуют себе молодые люди, мечтающие о море.\n" +
                        "А представляют они себя с кружкой кофе в одной руке, с биноклем в другой," +
                        " придерживающими тяжелый штурвал своей могучей капитанской грудью в белоснежной" +
                        " рубашке с погонами или в кителе, с капитанской фуражкой на голове..."
                ),
                NewsItem(
                    sourceInfo = getTestSourceInfo(),
                    timePassed = "вчера в 18:00",
                    imageUrl = "https://source.unsplash.com/LrMWHKqilUw/600x800",
                    imageWidth = 800, imageHeight = 600,
                    text = "Рекламная фотография"
                ),
                NewsItem(
                    sourceInfo = getTestSourceInfo(),
                    timePassed = "месяц назад",
                    imageUrl = "https://source.unsplash.com/PeFk7fzxTdk/600x800",
                    imageWidth = 600, imageHeight = 800,
                ),
                NewsItem(
                    sourceInfo = getTestSourceInfo(),
                    timePassed = "год назад",
                    imageUrl = "https://source.unsplash.com/USrZRcRS2Lw/600x800",
                    imageWidth = 600, imageHeight = 800,
                ),
            )
        )
    }.toList()

    private fun getTestSourceInfo() = NewsItemSourceInfo(
        "Иван Иванов",
        "https://sun2.megafon-nn.userapi.com/s/v1/ig2/" +
            "LHY02W7mmBoRPpvp9jhmB_cCfMF9zqSXWViuVNFFpjrtH2wShqMv" +
            "RpgwXfnoj8gCQWBrwqNtokrCldcLwGDvw1n3.jpg" +
            "?size=50x0&quality=96&crop=65,52,281,281&ava=1"
    )

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