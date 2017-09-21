@file:Suppress("unused")

package cc.aoeiuv020.comic.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ImageView
import cc.aoeiuv020.comic.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.alert

/**
 * 拓展，
 * Created by AoEiuV020 on 2017.09.12-18:33:43.
 */

fun <T : Any?> Observable<T>.async(): Observable<T> = this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun asyncLoadImage(image: ImageView, url: String) {
    Glide.with(image).load(url).into(image)
}

/**
 * 下载过程保持原来的图片，
 */
fun <TranscodeType> RequestBuilder<TranscodeType>.holdInto(image: ImageView)
        = apply(RequestOptions().placeholder(image.drawable)).into(image)

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun Context.alertError(message: String, e: Throwable) = alert(message + "\n" + e.message).show()

/**
 * 如果context已经销毁了，返回null,
 * 要是直接调用Glide.with，会报
 */
fun Context.glide(): RequestManager? = if (this is Activity && this.isDestroyed) null else Glide.with(this)

fun Context.glide(callback: (RequestManager) -> Unit) = glide()?.also { callback(it) }

fun Context.loading(dialog: ProgressDialog, id: Int) = loading(dialog, getString(R.string.loading, getString(id)))
fun Context.loading(dialog: ProgressDialog, str: String) = dialog.apply {
    setMessage(str)
    show()
}

fun Context.alertError(dialog: AlertDialog, str: String, e: Throwable) = alert(dialog, str + "\n" + e.message)
fun Context.alert(dialog: AlertDialog, messageId: Int) = alert(dialog, getString(messageId))
fun Context.alert(dialog: AlertDialog, messageId: Int, titleId: Int) = alert(dialog, getString(messageId), getString(titleId))
fun Context.alert(dialog: AlertDialog, message: String, title: String? = null) = dialog.apply {
    dialog.setMessage(message)
    title?.let {
        dialog.setTitle(title)
    }
    show()
}