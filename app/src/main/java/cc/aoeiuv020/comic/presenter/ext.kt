@file:Suppress("unused")

package cc.aoeiuv020.comic.presenter

import android.content.Context

/**
 * 拓展，主要对view层的拓展，
 * 为了在presenter层方便使用，
 * Created by AoEiuV020 on 2017.09.18-17:03:13.
 */

/**
 * 封装一些Context通用的方法，
 */
interface ContextView {
    val ctx: Context
}

fun ContextView.str(id: Int): String = ctx.getString(id)
fun ContextView.str(id: Int, vararg formatArgs: String): String = ctx.getString(id, *formatArgs)
