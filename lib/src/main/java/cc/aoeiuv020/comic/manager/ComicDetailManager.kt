package cc.aoeiuv020.comic.manager

import cc.aoeiuv020.comic.api.ComicDetailSpider
import cc.aoeiuv020.comic.model.ComicDetailModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 * 处理漫画详情面的爬虫和模型，
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicDetailManager : AnkoLogger {
    internal var comicDetailSpider: ComicDetailSpider? = null
    val comicDetailModel: ComicDetailModel?
        get() = comicDetailSpider?.let { ComicDetailModel(it) }

    fun reset(comicDetailSpider: ComicDetailSpider?) {
        debug { "reset " + comicDetailSpider?.name }
        this.comicDetailSpider = comicDetailSpider
        ComicManager.comicContentsManager.reset()
    }
}