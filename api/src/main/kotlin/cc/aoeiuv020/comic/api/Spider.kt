package cc.aoeiuv020.comic.api

/**
 * 所有爬虫类的基类，
 * 目前只有logger,
 * Created by AoEiuV020 on 17-5-31.
 */
abstract class Spider {
    protected val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)
}