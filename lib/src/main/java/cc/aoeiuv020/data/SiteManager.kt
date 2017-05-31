package cc.aoeiuv020.data

import cc.aoeiuv020.comic.api.site.SiteDAO
import cc.aoeiuv020.comic.api.site.SiteSpider
import cc.aoeiuv020.model.SiteModel

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
            ApiManager.classificationManager.reset()
        }
    val siteModels by lazy { siteSpiders.map { SiteModel(it) } }
}
