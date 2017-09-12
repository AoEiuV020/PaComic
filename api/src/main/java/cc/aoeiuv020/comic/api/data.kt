package cc.aoeiuv020.comic.api

/**
 * 相关的数据类定义在这文件里，
 * Created by AoEiuV020 on 2017.09.12-14:35:04.
 */

/**
 * 漫画网站信息，
 */
open class ComicSite(
        val name: String,
        val baseUrl: String,
        val logo: String
)

/**
 * 包含漫画列表的页面，
 * 比如，分类，搜索结果，
 *
 * @Deprecated
 */
open class ComicListPage(
        val url: String
)

/**
 * 漫画分类页面，
 * 该分类第一页地址，
 */
open class ComicGenre(
        val name: String,
        val url: String
)

/**
 * 漫画列表中的一个漫画，
 * @param url 该漫画详情页地址，
 */
open class ComicListItem(
        val name: String,
        val img: String,
        val url: String
)

/**
 * 漫画详情页，
 */
open class ComicDetail(
        val name: String,
        val bigImg: String,
        val info: String,
        val issues: List<ComicIssue>
)

/**
 * 漫画目录，
 * @param url 本章节第一页地址，
 */
open class ComicIssue(
        val name: String,
        val url: String
)

/**
 * 漫画页面，
 */
open class ComicPage(
        val url: String
)

/**
 * 漫画图片，
 * @param img 漫画图片地址，
 */
open class ComicImage(
        val img: String
)