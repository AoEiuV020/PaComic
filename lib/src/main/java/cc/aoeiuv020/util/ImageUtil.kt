package cc.aoeiuv020.util

import android.graphics.BitmapFactory
import android.widget.ImageView
import cc.aoeiuv020.lib.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 * Created by AoEiuV020 on 17-5-29.
 */
object ImageUtil {
    fun asyncSetImageUrl(image: ImageView, url: String) = with(image) {
        setTag(R.id.asyncSetImageUrl, url)
        doAsync {
            val bit = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
            uiThread {
                if (getTag(R.id.asyncSetImageUrl) == url && bit != null) {
                    setImageBitmap(bit)
                }
            }
        }
    }
}