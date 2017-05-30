package cc.aoeiuv020.comic.apitester

import android.app.ListActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import cc.aoeiuv020.comic.api.ApiManager
import cc.aoeiuv020.comic.api.site.Site
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ClassificationActivity : ListActivity() {
    private lateinit var site: Site
    private lateinit var listItems: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        siteIndex?.let { site = ApiManager.siteDAO.site(it) }
        doAsync {
            listItems = site.classifications.map { it.name }
            uiThread {
                listAdapter = ArrayAdapter(this@ClassificationActivity, android.R.layout.simple_list_item_1, listItems)
            }
        }
    }
}
