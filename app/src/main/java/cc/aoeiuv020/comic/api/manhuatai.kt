package cc.aoeiuv020.comic.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.jsoup.Jsoup
import java.net.URLEncoder


/**
 * 漫画台，
 * Created by AoEiuV020 on 2017.09.19-11:18:04.
 */
class ManhuataiContext : ComicContext() {
    /**
     * logo用了精灵图，不好办，截下来发到百度再取连接，
     * 地址虽然是jpg结尾，但文件还是支持背景透明的png,
     */
    private val site = ComicSite(
            name = "漫画台",
            baseUrl = "http://www.manhuatai.com",
            logo = "https://imgsa.baidu.com/forum/w%3D580/sign=68ba64a46e380cd7e61ea2e59145ad14/45e8c1afa40f4bfb23290540084f78f0f6361810.jpg"
    )

    override fun getComicSite(): ComicSite = site

    override fun getGenres(): List<ComicGenre> {
        val root = getHtml(site.baseUrl)
        val elements = root.select("#nav > div > ul > li > span > a:contains(漫)")
        return elements.map {
            val a = it
            ComicGenre(text(a), absHref(a))
        }
    }

    override fun getNextPage(genre: ComicGenre): ComicGenre? {
        if (isSearchResult(genre)) {
            return null
        }
        val root = getHtml(genre.url)
        val a = root.select("a:contains(下一页)").first()
        val url = absHref(a)
        return if (url.isEmpty()) {
            null
        } else {
            ComicGenre(genre.name, url)
        }
    }

    override fun getComicList(genre: ComicGenre): List<ComicListItem> {
        if (isSearchResult(genre)) {
            val json = Jsoup.connect(genre.url).apply { execute() }.response().body()
            val searchResultList = Gson().fromJson(json, SearchResultList::class.java)
            return searchResultList.map {
                // 搜索结果没有图片，拿网站logo顶一下，
                ComicListItem(it.cartoon_name, site.logo, absUrl("/${it.cartoon_id}/"), "${it.cartoon_status_id}, ${it.latest_cartoon_topic_name}")
            }
        }
        val root = getHtml(genre.url)
        val elements = root.select("a.sdiv")
        return elements.map {
            val a = it
            val img = it.select("img").first()
            val info = it.select("span.a , li:not(.title)").joinToString("\n") { text(it) }
            ComicListItem(title(a), img.attr("data-url"), absHref(a), info)
        }
    }

    class SearchResultList : ArrayList<SearchResultItem>()

    data class SearchResultItem(
            val cartoon_id: String, // kenan
            val cartoon_name: String, // 柯南
            val cartoon_status_id: String, // 连载
            val latest_cartoon_topic_name: String// 996话
    )

    override fun search(name: String): ComicGenre {
        return ComicGenre(name, searchUrl(name))
    }

    private fun searchUrl(name: String): String {
        val urlEncodedName = URLEncoder.encode(name, "UTF-8")
        return absUrl("/getjson.shtml?d=1505801840099&q=$urlEncodedName")
    }

    override fun isSearchResult(genre: ComicGenre): Boolean = genre.url.matches(Regex(".*/getjson.*"))

    override fun getComicDetail(comicListItem: ComicListItem): ComicDetail {
        val root = getHtml(comicListItem.url)
        val name = comicListItem.name
        val bigImg = src(root.select("#offlinebtn-container > img").first())
        val info = textWithNewLine(root.select("div.wz.clearfix > div").first(), 1)
        val issues = root.select("div.mhlistbody > ul > li > a").map {
            val a = it
            ComicIssue(text(a), absHref(a))
        }.asReversed()
        return ComicDetail(name, bigImg, info, issues)
    }

    /**
     * 这网站没有根据url跳页码，
     * 只能是直接在这里处理出图片地址塞进url里，
    关键js在这里，
    http://www.manhuatai.com/static/comicread.js?20170912181829
    开头”;new Function“改成"eval"
    结尾”();“删掉，
    给这网站反混淆，
    http://jsbeautifier.org/

    关键js是这段，这个反斜杆是为了避免kotlin的警告，\[chapter_id]
    n.getPicUrl = function(a) {
    var b = mh_info.comic_size || '';
    var c = lines\[chapter_id].use_line;
    if (c.indexOf("mhpic") == -1) {
    c += ":82"
    }
    var d = (parseInt(mh_info.startimg) + a - 1) + ".jpg" + b;
    var e = "http://" + c + '/comic/' + mh_info.imgpath + d;
    return e
    };
     */
    override fun getComicPages(comicIssue: ComicIssue): List<ComicPage> {
        val root = getHtml(comicIssue.url)
        val script = root.select("#comiclist > script:nth-child(2)").first()
        val mh_info_js = script.toString()
        logger.trace { mh_info_js }
        val mh_info_json = mh_info_js.pick("<script>var mh_info=(\\{[^}]*\\}).*")
        val mh_info = Gson().fromJson(mh_info_json, MhInfo::class.java)
        // 计算偏移, 第二个字符总是%,
        val offset = mh_info.imgpath[1] - '%'
        val imgpath = mh_info.imgpath.map { (it.toInt() - offset).toChar() }.joinToString("")
        logger.debug { mh_info }
        logger.trace { imgpath }
        val b = mh_info.comic_size ?: ""
        val c = "mhpic." + mh_info.domain
        return List(mh_info.totalimg) {
            val d = (mh_info.startimg + it).toString() + ".jpg" + b
            val e = "http://$c/comic/$imgpath$d"
            ComicPage(comicIssue.url + "#" + e)
        }
    }

    data class MhInfo(@SerializedName("startimg") val startimg: Int,
                      @SerializedName("totalimg") val totalimg: Int,
                      @SerializedName("pageid") val pageid: Int,
                      @SerializedName("comic_size") val comic_size: String?,
                      @SerializedName("domain") val domain: String,
                      @SerializedName("imgpath") val imgpath: String
    )

    override fun getComicImage(comicPage: ComicPage): ComicImage {
        val url = comicPage.url.split("#")[1]
        return ComicImage(url)
    }
}