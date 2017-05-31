package cc.aoeiuv020.data

import cc.aoeiuv020.comic.api.ApiManager
import cc.aoeiuv020.comic.api.site.SiteSniper
import cc.aoeiuv020.model.SiteModel

/**
 * 处理网站爬虫类和模型类，
 * 这里的方法都没有网络访问，
 */
class SiteManager {
    internal val siteSnipers by lazy { ApiManager.siteDAO.allSiteSnipers }
    val siteModel: SiteModel?
        get() = siteIndex?.let { siteModels[it] }
    internal var siteSpider: SiteSniper? = null
        get() = siteIndex?.let { siteSnipers[it] }
    var siteIndex: Int? = null
        set(value) {
            field = value
            Comic.classificationManager.reset()
        }
    val siteModels by lazy { siteSnipers.map { SiteModel(it) } }
}
