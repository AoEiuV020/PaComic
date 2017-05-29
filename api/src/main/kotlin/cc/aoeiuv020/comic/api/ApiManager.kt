package cc.aoeiuv020.comic.api

import cc.aoeiuv020.comic.api.site.SiteDAO

/**
 * Created by AoEiuV020 on 17-5-28.
 */
object ApiManager {
    val siteDAO by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SiteDAO() }
}