package cc.aoeiuv020.model

import cc.aoeiuv020.comic.api.site.SiteSpider

/**
 * Created by AoEiuV020 on 17-5-30.
 */
class SiteModel(siteSpider: SiteSpider) {
    val name = siteSpider.name
    val logoUrl = siteSpider.logoUrl
}
