package cc.aoeiuv020.comic.api.site

/**
 * Created by AoEiuV020 on 17-5-28.
 */
class SiteDAO {
    private val allSitesType = listOf<Class<out SiteSpider>>(Popomh::class.java)
    val allSiteSpiders by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { allSitesType.map { it.newInstance() } }
    fun site(index: Int): SiteSpider = allSiteSpiders[index]
}