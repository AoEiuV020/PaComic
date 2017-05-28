package cc.aoeiuv020.comic.apitester

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.TextView
import cc.aoeiuv020.comic.api.ApiManager
import cc.aoeiuv020.comic.api.site.Site
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class SiteTestActivity : ListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listItems = ApiManager.siteManger.sites
        listAdapter = object : BaseAdapter(), ListAdapter {
            override fun getItem(position: Int): Site = listItems[position]

            override fun getItemId(position: Int): Long = position.toLong()

            override fun getCount(): Int = listItems.size

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val ankoContext = AnkoContext.createReusable(this@SiteTestActivity)
                val view = convertView ?: ItemUI().createView(ankoContext)
                view.text = getItem(position).name
                return view
            }
        }
    }
}

class ItemUI : AnkoComponent<Any> {
    lateinit var name: TextView
    override fun createView(ui: AnkoContext<Any>) = with(ui) {
        verticalLayout {
            name = textView()
        }
    }
}
