package cc.aoeiuv020.comic.api.site

/**
 * Created by AoEiuV020 on 17-5-31.
 */
abstract class ComicDetailSpider : Spider() {
    abstract val name: String
    abstract val info: String
    abstract val imgUrl: String
}