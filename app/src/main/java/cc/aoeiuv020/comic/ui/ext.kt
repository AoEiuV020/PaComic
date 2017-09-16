package cc.aoeiuv020.comic.ui

import android.content.Context
import android.view.View
import android.widget.ImageView
import cc.aoeiuv020.comic.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.indeterminateProgressDialog

/**
 * 拓展，
 * Created by AoEiuV020 on 2017.09.12-18:33:43.
 */

/**
 * 展示进度条，
 */
fun Context.loading(str: String = "") = indeterminateProgressDialog(getString(R.string.loading, str))

fun Context.loading(id: Int) = loading(getString(id))


fun <T : Any?> Observable<T>.async(): Observable<T> = this
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())

@Suppress("unused")
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
