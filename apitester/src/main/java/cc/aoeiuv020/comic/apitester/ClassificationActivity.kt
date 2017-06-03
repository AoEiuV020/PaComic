package cc.aoeiuv020.comic.apitester

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import cc.aoeiuv020.comic.manager.ComicManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ClassificationActivity : ListActivity() {
    private lateinit var listItems: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doAsync {
            listItems = ComicManager.classificationManager.classificationModels?.map { it.name }
                    ?: return@doAsync
            uiThread {
                listAdapter = ArrayAdapter(this@ClassificationActivity, android.R.layout.simple_list_item_1, listItems)
            }
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        ComicManager.classificationManager.classificationIndex = position
        finish()
    }
}
