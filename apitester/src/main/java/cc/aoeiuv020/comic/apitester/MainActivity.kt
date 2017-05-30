package cc.aoeiuv020.comic.apitester

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import cc.aoeiuv020.comic.api.ApiManager
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.verticalLayout

var siteIndex: Int? = null

class MainActivity : Activity() {
    private lateinit var bSite: Button
    private val siteDao = ApiManager.siteDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            bSite = button("Site") {
                onClick {
                    startActivityForResult<SiteActivity>(0)
                }
            }
            button("Classification") {
                onClick {
                    siteIndex?.let { startActivity<ClassificationActivity>() }
                            ?: bSite.callOnClick()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                siteIndex?.let {
                    bSite.text = siteDao.site(it).name
                }
            }
        }
    }
}
