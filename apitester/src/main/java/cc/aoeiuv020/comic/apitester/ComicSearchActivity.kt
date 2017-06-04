package cc.aoeiuv020.comic.apitester

import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import cc.aoeiuv020.comic.manager.ComicManager
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onItemClick

class ComicSearchActivity : Activity() {
    lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            val et = editText {
                /*
                text = "风起"
                大概是bug吧，
                Type mismatch.
                Required:
                Editable!
                Found:
                String
                 */
                setText("风起")
            }
            button("搜索") {
                onClick {
                    val name = et.text.toString()
                    doAsync {
                        val listItems = ComicManager.comicSearchManager.search(name)
                                ?: return@doAsync
                        uiThread {
                            listView.adapter = ComicItemAdapter(this@ComicSearchActivity, listItems)
                        }
                    }
                }
            }
            listView = listView {
                onItemClick { _, _, position, _ ->
                    doAsync {
                        ComicManager.comicSearchManager.comicIndex = position
                    }
                    finish()
                }
            }
        }
    }
}
