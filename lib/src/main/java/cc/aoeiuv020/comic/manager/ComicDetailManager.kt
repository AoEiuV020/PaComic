package cc.aoeiuv020.comic.manager

import cc.aoeiuv020.comic.api.ComicDetailSpider
import cc.aoeiuv020.comic.model.ComicDetailModel

/**
 * 处理漫画详情面的爬虫和模型，
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicDetailManager {
    internal val comicDetailSpider: ComicDetailSpider?
        get() = ApiManager.comicListManager.comicItemSpider?.comicDetail
    val comicDetailModel: ComicDetailModel?
        get() = comicDetailSpider?.let { ComicDetailModel(it) }

    fun reset() {
        ApiManager.comicContentsManager.reset()
    }
}