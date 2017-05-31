package cc.aoeiuv020.comic.apitester

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import cc.aoeiuv020.data.ApiManager
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : Activity() {
    private lateinit var bSite: Button
    private lateinit var bClassification: Button
    private lateinit var bComicItem: Button
    private lateinit var bComicDetail: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            bSite = button {
                onClick {
                    startActivity<SiteActivity>()
                }
            }
            bClassification = button {
                onClick {
                    ApiManager.siteManager.siteModel?.let { startActivity<ClassificationActivity>() }
                            ?: bSite.callOnClick()
                }
            }
            bComicItem = button {
                onClick {
                    ApiManager.classificationManager.classificationModel?.let { startActivity<ComicListActivity>() }
                            ?: bClassification.callOnClick()
                }
            }
            bComicDetail = button {
                onClick {
                    ApiManager.comicListManager.comicListItemModel?.let { startActivity<ComicDetailActivity>() }
                            ?: bComicItem.callOnClick()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        doAsync {
            val siteName = ApiManager.siteManager.siteModel?.name
            val classificationName = ApiManager.classificationManager.classificationModel?.name
            val comicItemName = ApiManager.comicListManager.comicListItemModel?.name
            val comicDetailName = ApiManager.comicDetailManager.comicDetailModel?.name
            uiThread {
                bSite.text = siteName ?: "网站"
                bClassification.text = classificationName ?: "分类"
                bComicItem.text = comicItemName ?: "漫画"
                bComicDetail.text = comicDetailName ?: "漫画"
            }
        }
    }
}
