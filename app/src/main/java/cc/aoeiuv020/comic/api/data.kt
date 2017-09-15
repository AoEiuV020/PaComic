package cc.aoeiuv020.comic.api

import java.io.Serializable

/**
 * 相关的数据类定义在这文件里，
 * Created by AoEiuV020 on 2017.09.12-14:35:04.
 */

/**
 * 基类，
 */
open class Data : Serializable

/**
 * 漫画网站信息，
 */
data class ComicSite(
        val name: String,
        val baseUrl: String,
        val logo: String
) : Data()

/**
 * 包含漫画列表的页面，
 * 比如，分类，搜索结果，
 */
@Deprecated("没设计好")
data class ComicListPage(
        val url: String
) : Data()

/**
 * 漫画分类页面，
 * 该分类第一页地址，
 */
data class ComicGenre(
        val name: String,
        val url: String
) : Data()

/**
 * 漫画列表中的一个漫画，
 * @param url 该漫画详情页地址，
 */
data class ComicListItem(
        val name: String,
        val img: String,
        val url: String
) : Data()

/**
 * 漫画详情页，
 * @param issuesAsc 升序章节，
 */
data class ComicDetail(
        val name: String,
        val bigImg: String,
        val info: String,
        val issuesAsc: List<ComicIssue>
) : Data()

/**
 * 漫画目录，
 * @param url 本章节第一页地址，
 */
data class ComicIssue(
        /**
         * 章节名不包括漫画名，
         */
        val name: String,
        val url: String
) : Data()

/**
 * 漫画页面，
 */
data class ComicPage(
        val url: String
) : Data()

/**
 * 漫画图片，
 * @param img 漫画图片地址，
 */
data class ComicImage(
        val img: String
) : Data()
