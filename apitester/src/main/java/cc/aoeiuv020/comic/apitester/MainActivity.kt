package cc.aoeiuv020.comic.apitester

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import cc.aoeiuv020.comic.manager.ApiManager
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : Activity() {
    private lateinit var bSite: Button
    private lateinit var bClassification: Button
    private lateinit var bComicItem: Button
    private lateinit var bComicDetail: Button
    private lateinit var bComicContents: Button
    private lateinit var bComicPage: Button

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
                    doAsync {
                        val model = ApiManager.siteManager.siteModel
                        uiThread {
                            model?.let { startActivity<ClassificationActivity>() }
                                    ?: bSite.callOnClick()
                        }
                    }
                }
            }
            bComicItem = button {
                onClick {
                    doAsync {
                        val model = ApiManager.classificationManager.classificationModel
                        model?.let { startActivity<ComicListActivity>() }
                                ?: bClassification.callOnClick()
                    }
                }
            }
            bComicDetail = button {
                onClick {
                    doAsync {
                        val model = ApiManager.comicListManager.comicListItemModel
                        model?.let { startActivity<ComicDetailActivity>() }
                                ?: bComicItem.callOnClick()
                    }
                }
            }
            bComicContents = button {
                onClick {
                    doAsync {
                        val model = ApiManager.comicDetailManager.comicDetailModel
                        model?.let { startActivity<ComicContentsActivity>() }
                                ?: bComicDetail.callOnClick()
                    }
                }
            }
            bComicPage = button {
                onClick {
                    doAsync {
                        val model = ApiManager.comicContentsManager.comicContentsModel
                        model?.let { startActivity<ComicPageActivity>() }
                                ?: bComicContents.callOnClick()
                    }
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
            val comicContentsName = ApiManager.comicContentsManager.comicContentsModel?.name
            val comicPagesCount = ApiManager.comicPageManager.comicPagesCountModel?.pagesCount
            uiThread {
                bSite.text = siteName ?: "网站"
                bClassification.text = classificationName ?: "分类"
                bComicItem.text = comicItemName ?: "漫画列表"
                bComicDetail.text = comicDetailName ?: "漫画详情"
                bComicContents.text = comicContentsName ?: "目录"
                bComicPage.text = comicPagesCount?.toString() ?: "漫画图片"
            }
        }
    }
}
