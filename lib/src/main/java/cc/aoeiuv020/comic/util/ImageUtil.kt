package cc.aoeiuv020.comic.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 * Created by AoEiuV020 on 17-5-29.
 */
object ImageUtil : AnkoLogger {
    fun asyncSetImageUrl(image: ImageView, url: String) {
        debug { "${image.hashCode()}, $url" }
        Glide.with(image).load(url).into(image)
    }
}