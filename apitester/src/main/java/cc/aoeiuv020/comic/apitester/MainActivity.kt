package cc.aoeiuv020.comic.apitester

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import cc.aoeiuv020.comic.manager.ComicManager
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
                        val model = ComicManager.siteManager.siteModel
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
                        val model = ComicManager.classificationManager.classificationModel
                        model?.let { startActivity<ComicListActivity>() }
                                ?: bClassification.callOnClick()
                    }
                }
            }
            bComicDetail = button {
                onClick {
                    doAsync {
                        val model = ComicManager.comicListManager.comicListItemModel
                        model?.let { startActivity<ComicDetailActivity>() }
                                ?: bComicItem.callOnClick()
                    }
                }
            }
            bComicContents = button {
                onClick {
                    doAsync {
                        val model = ComicManager.comicDetailManager.comicDetailModel
                        model?.let { startActivity<ComicContentsActivity>() }
                                ?: bComicDetail.callOnClick()
                    }
                }
            }
            bComicPage = button {
                onClick {
                    doAsync {
                        val model = ComicManager.comicContentsManager.comicContentsModel
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
            val siteName = ComicManager.siteManager.siteModel?.name
            val classificationName = ComicManager.classificationManager.classificationModel?.name
            val comicItemName = ComicManager.comicListManager.comicListItemModel?.name
            val comicDetailName = ComicManager.comicDetailManager.comicDetailModel?.name
            val comicContentsName = ComicManager.comicContentsManager.comicContentsModel?.name
            val comicPagesCount = ComicManager.comicPageManager.comicPagesCountModel?.pagesCount
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
