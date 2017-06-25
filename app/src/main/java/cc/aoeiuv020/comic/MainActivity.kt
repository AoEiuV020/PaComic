package cc.aoeiuv020.comic

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import cc.aoeiuv020.comic.manager.ComicManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AnkoLogger {
    val GROUP_ID = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    ComicManager.comicListManager.comicListItemModels?.apply {
                        uiThread {
                            listView.run {
                                adapter = ComicListAdapter(this@MainActivity, this@apply)
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
                                this@apply.forEachIndexed { index, it ->
                                    add(GROUP_ID, index, index, it.name)
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
