package cc.aoeiuv020.comic.api.site

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 所有爬虫类的基类，
 * 目前只有logger,
 * Created by AoEiuV020 on 17-5-31.
 */
abstract class Spider {
    internal val logger: Logger = LoggerFactory.getLogger(this.javaClass)
}