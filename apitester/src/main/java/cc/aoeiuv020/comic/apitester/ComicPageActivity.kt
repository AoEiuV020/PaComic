package cc.aoeiuv020.comic.apitester

import android.app.Activity
import android.os.Bundle
import cc.aoeiuv020.comic.manager.ApiManager
import cc.aoeiuv020.comic.util.ImageUtil
import org.jetbrains.anko.*

class ComicPageActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            val imgImageView = imageView().lparams(matchParent, matchParent)
            doAsync {
                val comicPageModel = ApiManager.comicPageManager.comicPageModel
                        ?: return@doAsync
                uiThread {
                    ImageUtil.asyncSetImageUrl(imgImageView, comicPageModel.imgUrl)
                }
            }
        }
    }
}
