package cc.aoeiuv020.comic.api

import cc.aoeiuv020.comic.api.dm5.Dm5
import cc.aoeiuv020.comic.api.popomh.Popomh

/**
 * Created by AoEiuV020 on 17-5-28.
 */
class SiteDAO {
    private val allSitesType = listOf<Class<out SiteSpider>>(Popomh::class.java, Dm5::class.java)
    val allSiteSpiders by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { allSitesType.map { it.newInstance() } }
}