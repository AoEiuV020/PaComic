package cc.aoeiuv020.comic.api.site

/**
 * Created by AoEiuV020 on 17-5-28.
 */
class SiteDAO {
    private val allSitesType = listOf<Class<out Site>>(Popomh::class.java)
    val allSites by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { allSitesType.map { it.newInstance() } }
}