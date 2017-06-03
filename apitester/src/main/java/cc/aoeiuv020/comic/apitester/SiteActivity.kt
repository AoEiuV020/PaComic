package cc.aoeiuv020.comic.apitester

import android.app.ListActivity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cc.aoeiuv020.comic.manager.ComicManager
import cc.aoeiuv020.comic.model.SiteModel
import cc.aoeiuv020.comic.util.ImageUtil
import org.jetbrains.anko.*

class SiteActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doAsync {
            val listItems = ComicManager.siteManager.siteModels
            uiThread {
                listAdapter = object : BaseAdapter(), ListAdapter {
                    override fun getItem(position: Int): SiteModel = listItems[position]

                    override fun getItemId(position: Int): Long = 0L

                    override fun getCount(): Int = listItems.size

                    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                        val view = if (convertView == null) {
                            val ankoContext = AnkoContext.createReusable(this@SiteActivity)
                            val ui = ItemUI()
                            ui.createView(ankoContext).apply {
                                tag = ui
                            }
                        } else convertView
                        (view.tag as ItemUI).apply(getItem(position))
                        return view
                    }
                }
            }
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        ComicManager.siteManager.siteIndex = position
        finish()
    }

    class ItemUI : AnkoComponent<Context> {
        lateinit var name: TextView
        lateinit var image: ImageView

        override fun createView(ui: AnkoContext<Context>) = with(ui) {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                image = imageView(android.R.drawable.ic_menu_report_image)
                        .lparams(dip(200), dip(50))
                name = textView()
            }
        }

        fun apply(site: SiteModel) {
            name.text = site.name
            ImageUtil.asyncSetImageUrl(image, site.logoUrl)
        }
    }
}
