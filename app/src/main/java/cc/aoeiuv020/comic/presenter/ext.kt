@file:Suppress("unused")

package cc.aoeiuv020.comic.presenter

import android.content.Context
import cc.aoeiuv020.comic.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.browse
import org.jetbrains.anko.indeterminateProgressDialog

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
fun ContextView.browse(url: String) = ctx.browse(url)

/**
 * 封装一些对话框相关的方法，
 */
interface AlertableView : ContextView


fun AlertableView.loading(str: String = "") = ctx.indeterminateProgressDialog(str(R.string.loading, str))
fun AlertableView.loading(id: Int) = loading(str(id))

fun AlertableView.alertError(str: String, e: Throwable) = ctx.alert(str + "\n" + e.message).show()

fun AlertableView.alertError(id: Int, e: Throwable) = alertError(str(id), e)

fun AlertableView.alert(message: String, title: String? = null) = ctx.alert(message, title).show()

fun AlertableView.alert(messageId: Int) = alert(str(messageId))
fun AlertableView.alert(messageId: Int, titleId: Int) = alert(str(messageId), str(titleId))

