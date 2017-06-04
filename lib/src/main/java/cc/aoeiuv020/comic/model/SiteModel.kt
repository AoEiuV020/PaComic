package cc.aoeiuv020.comic.model

import cc.aoeiuv020.comic.api.SiteSpider

/**
 * Created by AoEiuV020 on 17-5-30.
 */
class SiteModel(siteSpider: SiteSpider) {
    val name = siteSpider.name
    val logoUrl = siteSpider.logoUrl
}
