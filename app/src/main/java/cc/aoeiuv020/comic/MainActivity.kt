package cc.aoeiuv020.comic

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import cc.aoeiuv020.comic.manager.ComicManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AnkoLogger {
    val GROUP_ID = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        search.setOnClickListener {
            alert("搜索") {
                val text = EditText(this@MainActivity)
                customView {
                    addView(text, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                }
                okButton {
                    val dialog = loading()
                    doAsync {
                        ComicManager.comicSearchManager.search(text.text.toString())?.let { comicList ->
                            uiThread {
                                dialog.cancel()
                                listView.run {
                                    adapter = ComicListAdapter(this@MainActivity, comicList)
                                    setOnItemClickListener { _, _, position, _ ->
                                        ComicManager.comicSearchManager.comicIndex = position
                                        startActivity<ComicDetailActivity>()
                                    }
                                }
                            }
                        }
                    }
                }
            }.show()
        }
        nav_view.setNavigationItemSelectedListener(this)
        ComicManager.siteManager.siteIndex ?: showSites()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.groupId) {
            GROUP_ID -> {
                ComicManager.classificationManager.classificationIndex = item.order
                val dialog = loading()
                doAsync {
                    ComicManager.comicListManager.comicListItemModels?.let { comicList ->
                        uiThread {
                            listView.run {
                                adapter = ComicListAdapter(this@MainActivity, comicList)
                                setOnItemClickListener { _, _, position, _ ->
                                    ComicManager.comicListManager.comicIndex = position
                                    startActivity<ComicDetailActivity>()
                                }
                            }
                        }
                    }
                    dialog.cancel()
                }
            }
            else -> when (item.itemId) {
                R.id.select_sites -> showSites()
                R.id.settings -> {
                    Snackbar.make(drawer_layout, "没实现", Snackbar.LENGTH_SHORT).show()
                }
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showSites() {
        listView.run {
            adapter = SiteListAdapter(this@MainActivity)
            setOnItemClickListener { _, _, position, _ ->
                drawer_layout.openDrawer(GravityCompat.START)
                ComicManager.siteManager.siteIndex = position
                val dialog = loading()
                doAsync {
                    ComicManager.classificationManager.classificationModels?.apply {
                        uiThread {
                            nav_view.menu.run {
                                removeGroup(GROUP_ID)
                                this@apply.forEachIndexed { index, classificationModel ->
                                    add(GROUP_ID, index, index, classificationModel.name)
                                }
                            }
                        }

                    }
                    dialog.cancel()
                }
            }
        }
    }
}
