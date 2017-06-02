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
import cc.aoeiuv020.comic.model.ComicPageModel
import cc.aoeiuv020.comic.util.ImageUtil
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.viewPager

class ComicPageActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            viewPager {
                doAsync {
                    val comicPageModel = ApiManager.comicPageManager.comicPageModel
                            ?: return@doAsync
                    uiThread {
                        adapter = GalleryAdapter(comicPageModel)
                    }
                }
            }
        }
    }

    class GalleryAdapter(val comicPageModel: ComicPageModel) : PagerAdapter(), AnkoLogger {
        val views: MutableList<View> = ArrayList(count)

        init {
            debug("init ${comicPageModel.imgUrl}")
        }

        override fun getCount(): Int = comicPageModel.pagesCount
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

    class PagerUI : AnkoComponent<Context> {
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
                ImageUtil.asyncSetImageUrl(image, url)
            }
        }
    }
}
