package cc.aoeiuv020.comic.apitester

import android.app.Activity
import android.os.Bundle
import cc.aoeiuv020.comic.manager.ComicManager
import cc.aoeiuv020.comic.util.ImageUtil
import org.jetbrains.anko.*

class ComicDetailActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            val imgImageView = imageView().lparams(matchParent, dip(400))
            val infoTextView = textView()
            doAsync {
                val comicModel = ComicManager.comicDetailManager.comicDetailModel
                        ?: return@doAsync
                uiThread {
                    infoTextView.text = comicModel.info
                    ImageUtil.asyncSetImageUrl(imgImageView, comicModel.imgUrl)
                }
            }
        }
    }
}
