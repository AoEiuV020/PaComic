package cc.aoeiuv020.comic.api.site

import org.slf4j.LoggerFactory

/**
 * Created by AoEiuV020 on 17-5-28.
 */
abstract class Site {
    val logger = LoggerFactory.getLogger(this.javaClass)
    abstract val name: String
    abstract val baseUrl: String
    abstract val logoUrl: String
    abstract val classifications: List<Classification>
}