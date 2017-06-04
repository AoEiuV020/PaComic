package cc.aoeiuv020.comic.manager

import cc.aoeiuv020.comic.api.SiteDAO
import cc.aoeiuv020.comic.api.SiteSpider
import cc.aoeiuv020.comic.model.SiteModel

/**
 * 处理网站爬虫类和模型类，
 * 这里的方法都没有网络访问，
 */
class SiteManager {
    val siteDAO by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SiteDAO() }
    internal val siteSpiders by lazy { siteDAO.allSiteSpiders }
    val siteModel: SiteModel?
        get() = siteIndex?.let { siteModels[it] }
    internal var siteSpider: SiteSpider? = null
        get() = siteIndex?.let { siteSpiders[it] }
    var siteIndex: Int? = null
        set(value) {
            field = value
            ComicManager.classificationManager.reset()
            ComicManager.comicSearchManager.reset()
        }
    val siteModels by lazy { siteSpiders.map { SiteModel(it) } }
}
