package cc.aoeiuv020.data

import cc.aoeiuv020.comic.api.ApiManager
import cc.aoeiuv020.comic.api.site.SiteSpider
import cc.aoeiuv020.model.SiteModel

/**
 * 处理网站爬虫类和模型类，
 * 这里的方法都没有网络访问，
 */
class SiteManager {
    internal val siteSpiders by lazy { ApiManager.siteDAO.allSiteSpiders }
    val siteModel: SiteModel?
        get() = siteIndex?.let { siteModels[it] }
    internal var siteSpider: SiteSpider? = null
        get() = siteIndex?.let { siteSpiders[it] }
    var siteIndex: Int? = null
        set(value) {
            field = value
            Comic.classificationManager.reset()
        }
    val siteModels by lazy { siteSpiders.map { SiteModel(it) } }
}
