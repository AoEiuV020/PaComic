package cc.aoeiuv020.comic

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import cc.aoeiuv020.comic.manager.ComicManager
import cc.aoeiuv020.comic.util.ImageUtil
import kotlinx.android.synthetic.main.activity_comic_detail.*
import kotlinx.android.synthetic.main.content_comic_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ComicDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_detail)
        setSupportActionBar(toolbar)

        val dialog = loading()
        doAsync {
            ComicManager.comicDetailManager.comicDetailModel?.apply {
                uiThread {
                    toolbar_layout.title = this@apply.name
                    ImageUtil.asyncSetImageUrl(image, this@apply.imgUrl)
                }
            }
            ComicManager.comicContentsManager.comicContentsModels?.apply {
                uiThread {
                    recyclerView.adapter = ComicDetailAdapter(this@ComicDetailActivity, this@apply)
                    recyclerView.layoutManager = LinearLayoutManager(this@ComicDetailActivity)
                }
            }
            dialog.cancel()
        }
    }
}
