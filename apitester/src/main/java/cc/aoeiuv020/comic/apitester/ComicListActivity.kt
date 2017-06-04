package cc.aoeiuv020.comic.apitester

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import cc.aoeiuv020.comic.manager.ComicManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ComicListActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doAsync {
            val listItems = ComicManager.comicListManager.comicListItemModels?.let { it }
                    ?: return@doAsync
            uiThread {
                listAdapter = ComicItemAdapter(this@ComicListActivity, listItems)
            }
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        doAsync {
            ComicManager.comicListManager.comicIndex = position
        }
        finish()
    }
}
