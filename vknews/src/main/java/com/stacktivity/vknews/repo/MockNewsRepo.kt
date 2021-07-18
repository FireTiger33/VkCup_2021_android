package com.stacktivity.vknews.repo

import com.stacktivity.vknews.model.NewsItem
import com.stacktivity.vknews.model.NewsItemSourceInfo
import com.stacktivity.vknews.utils.format
import com.vk.sdk.api.newsfeed.dto.NewsfeedNewsfeedItem
import com.vk.sdk.api.wall.dto.WallWallpostAttachmentType
import org.ocpsoft.prettytime.PrettyTime
import java.util.Locale


/**
 * Used for test mode with out login to account vk
 */
object MockNewsRepo : NewsRepository {

    private val news: MutableList<NewsItem> = mutableListOf()

    /**
     * Uses VK API newsfeed.get method to get a news posts
     *
     * @return news with fetched items
     */
    override suspend fun fetchNews(): List<NewsItem> = news.apply {
        addAll(
            arrayListOf(
                NewsItem(
                    sourceInfo = getSourceInfo(),
                    timePassed = "час назад",
                    imageUrl = "https://source.unsplash.com/Xq1ntWruZQI/600x800",
                    imageWidth = 600, imageHeight = 800,
                ),
                NewsItem(
                    sourceInfo = getSourceInfo(),
                    timePassed = "три часа назад",
                    imageUrl = "https://source.unsplash.com/NYyCqdBOKwc/600x800",
                    imageWidth = 600, imageHeight = 800,
                    text = "Kyoto: Fushimi Inari Shrine"
                ),
                NewsItem(
                    sourceInfo = getSourceInfo(),
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
                    sourceInfo = getSourceInfo(),
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
                    sourceInfo = getSourceInfo(),
                    timePassed = "вчера в 18:00",
                    imageUrl = "https://source.unsplash.com/LrMWHKqilUw/600x800",
                    imageWidth = 800, imageHeight = 600,
                    text = "Рекламная фотография"
                ),
                NewsItem(
                    sourceInfo = getSourceInfo(),
                    timePassed = "месяц назад",
                    imageUrl = "https://source.unsplash.com/PeFk7fzxTdk/600x800",
                    imageWidth = 600, imageHeight = 800,
                ),
                NewsItem(
                    sourceInfo = getSourceInfo(),
                    timePassed = "год назад",
                    imageUrl = "https://source.unsplash.com/USrZRcRS2Lw/600x800",
                    imageWidth = 600, imageHeight = 800,
                ),
            )
        )
    }.toList()

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


    override fun removeItemAt(pos: Int): List<NewsItem> {
        return news.apply { removeAt(pos) }.toList()
    }

    fun getSourceInfo() = NewsItemSourceInfo(
        "Иван Иванов",
        "https://sun2.megafon-nn.userapi.com/s/v1/ig2/" +
            "LHY02W7mmBoRPpvp9jhmB_cCfMF9zqSXWViuVNFFpjrtH2wShqMv" +
            "RpgwXfnoj8gCQWBrwqNtokrCldcLwGDvw1n3.jpg" +
            "?size=50x0&quality=96&crop=65,52,281,281&ava=1"
    )

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