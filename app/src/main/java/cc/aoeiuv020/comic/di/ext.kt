package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicContext

/**
 * 定义拓展，
 * Created by AoEiuV020 on 2017.09.12-17:53:32.
 */

internal fun ctx(url: String): ComicContext = ComicContext.getComicContext(url)