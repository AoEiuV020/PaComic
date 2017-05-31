package cc.aoeiuv020.comic.apitester

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import cc.aoeiuv020.data.Comic
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : Activity() {
    private lateinit var bSite: Button
    private lateinit var bClassification: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            bSite = button("SiteSpider") {
                onClick {
                    startActivity<SiteActivity>()
                }
            }
            bClassification = button("ClassificationSpider") {
                onClick {
                    Comic.siteManager.siteModel?.let { startActivity<ClassificationActivity>() }
                            ?: bSite.callOnClick()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        doAsync {
            val siteName = Comic.siteManager.siteModel?.name
            val classificationName = Comic.classificationManager.classificationModel?.name
            uiThread {
                bSite.text = siteName ?: "网站"
                bClassification.text = classificationName ?: "分类"
            }
        }
    }
}
