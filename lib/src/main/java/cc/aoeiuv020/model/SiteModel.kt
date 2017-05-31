package cc.aoeiuv020.model

import cc.aoeiuv020.comic.api.site.SiteSniper

/**
 * Created by AoEiuV020 on 17-5-30.
 */
data class SiteModel(val name: String, val logoUrl: String) {
    constructor(siteSniper: SiteSniper) : this(siteSniper.name, siteSniper.logoUrl)
}
