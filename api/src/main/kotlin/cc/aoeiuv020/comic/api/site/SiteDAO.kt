package cc.aoeiuv020.comic.api.site

import cc.aoeiuv020.comic.api.site.popomh.Popomh

/**
 * Created by AoEiuV020 on 17-5-28.
 */
class SiteDAO {
    private val allSitesType = listOf<Class<out SiteSpider>>(Popomh::class.java)
    val allSiteSpiders by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { allSitesType.map { it.newInstance() } }
}