@file:Suppress("unused")

package cc.aoeiuv020.comic.api

import org.slf4j.Logger

/**
 * 定义slf4j一系列拓展，
 * 主要就是先判断再执行lambda,
 * Created by AoEiuV020 on 2017.09.17-11:37:41.
 */

inline fun Logger.trace(message: () -> Any?) {
    if (isTraceEnabled) {
        trace("{}", message().toString())
    }
}

inline fun Logger.debug(message: () -> Any?) {
    if (isDebugEnabled) {
        debug("{}", message().toString())
    }
}

inline fun Logger.info(message: () -> Any?) {
    if (isInfoEnabled) {
        info("{}", message().toString())
    }
}

inline fun Logger.warn(message: () -> Any?) {
    if (isWarnEnabled) {
        warn("{}", message().toString())
    }
}

inline fun Logger.error(message: () -> Any?) {
    if (isErrorEnabled) {
        error("{}", message().toString())
    }
}

inline fun Logger.error(e: Throwable, message: () -> Any?) {
    if (isErrorEnabled) {
        error(message().toString(), e)
    }
}
