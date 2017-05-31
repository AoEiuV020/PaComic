package cc.aoeiuv020.model

import cc.aoeiuv020.comic.api.site.SiteSpider

/**
 * Created by AoEiuV020 on 17-5-30.
 */
data class SiteModel(val name: String, val logoUrl: String) {
    constructor(siteSpider: SiteSpider) : this(siteSpider.name, siteSpider.logoUrl)
}
