package cc.aoeiuv020.comic.apitester

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cc.aoeiuv020.comic.manager.ApiManager
import cc.aoeiuv020.comic.model.ComicPagesCountModel
import cc.aoeiuv020.comic.util.ImageUtil
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.viewPager

class ComicPageActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            viewPager {
                doAsync {
                    val comicPagesCountModel = ApiManager.comicPageManager.comicPagesCountModel
                            ?: return@doAsync
                    uiThread {
                        adapter = GalleryAdapter(comicPagesCountModel)
                    }
                }
            }
        }
    }

    class GalleryAdapter(val comicPageCountModel: ComicPagesCountModel) : PagerAdapter(), AnkoLogger {
        val views: MutableList<View> = ArrayList(count)

        override fun getCount(): Int = comicPageCountModel.pagesCount
        override fun isViewFromObject(p0: View?, p1: Any?): Boolean {
            return p0 === p1
        }

        @SuppressLint("SetTextI18n")
        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            debug("instantiateItem $position")
            container!!
            val root: View
            val ui: PagerUI
            if (views.size > position) {
                root = views[position]
                ui = root.tag as PagerUI
            } else {
                val ankoContext = AnkoContext.createReusable(container.context)
                ui = PagerUI()
                root = ui.createView(ankoContext).apply {
                    tag = ui
                }
                views.add(root)
            }
            ui.apply(position)
            container.addView(root)
            return root
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            debug("destroyItem $position")
            container?.removeView(views[position])
        }
    }

    class PagerUI : AnkoComponent<Context>, AnkoLogger {
        lateinit var image: ImageView
        override fun createView(ui: AnkoContext<Context>): View = with(ui) {
            verticalLayout {
                image = imageView(android.R.drawable.ic_menu_report_image)
                        .lparams(matchParent, matchParent)
            }
        }

        fun apply(position: Int) {
            doAsync {
                val url = ApiManager.comicPageManager.comicPageModelAt(position)?.imgUrl
                        ?: return@doAsync
                debug { "<$position> $url" }
                ImageUtil.asyncSetImageUrl(image, url)
            }
        }
    }
}
