package cc.aoeiuv020.comic.api.site

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by AoEiuV020 on 17-5-31.
 */
abstract class Spider {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)
}