package cc.aoeiuv020.comic.ui

import android.content.Context
import android.widget.ImageView
import cc.aoeiuv020.comic.R
import com.bumptech.glide.Glide
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
fun Context.loading() = indeterminateProgressDialog(R.string.loading)

fun <T : Any?> Observable<T>.async() = this
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())

fun asyncLoadImage(image: ImageView, url: String) {
    Glide.with(image).load(url).into(image)
}
