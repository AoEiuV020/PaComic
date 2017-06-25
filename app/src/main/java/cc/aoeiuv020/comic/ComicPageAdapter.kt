package cc.aoeiuv020.comic

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import cc.aoeiuv020.comic.manager.ComicManager
import cc.aoeiuv020.comic.model.ComicPagesCountModel
import cc.aoeiuv020.comic.util.ImageUtil
import kotlinx.android.synthetic.main.comic_page_item.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * Created by AoEiuV020 on 17-6-25.
 */
class ComicPageAdapter(val context: Context, val countModel: ComicPagesCountModel) : PagerAdapter() {
    val views: LinkedList<View> = LinkedList()
    override fun isViewFromObject(view: View?, obj: Any?) = view === obj
    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val root = if (views.isNotEmpty())
            views.pop()
        else
            View.inflate(context, R.layout.comic_page_item, null).apply {
                setOnClickListener {
                    (context as ComicPageActivity).toggle()
                }
            }
        doAsync {
            ComicManager.comicPageManager.comicPageModelAt(position)?.apply {
                uiThread {
                    ImageUtil.asyncSetImageUrl(root.image, this@apply.imgUrl)
                }
            }
        }
        container?.addView(root)
        return root
    }

    override fun destroyItem(container: ViewGroup?, position: Int, obj: Any?) {
        val view = obj as View
        container?.removeView(view)
        views.push(view)
    }

    override fun getCount() = countModel.pagesCount
}