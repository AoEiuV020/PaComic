@file:Suppress("DEPRECATION")

package cc.aoeiuv020.comic.ui

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import cc.aoeiuv020.comic.R
import cc.aoeiuv020.comic.api.ComicImage
import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.api.ComicPage
import cc.aoeiuv020.comic.presenter.ComicPagePresenter
import cc.aoeiuv020.comic.ui.base.ComicPageBaseFullScreenActivity
import com.boycy815.pinchimageview.PinchImageView
import com.boycy815.pinchimageview.huge.HugeUtil
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.activity_comic_page.*
import kotlinx.android.synthetic.main.comic_page_item.view.*
import kotlinx.android.synthetic.main.comic_page_item_loading.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.browse
import org.jetbrains.anko.debug
import org.jetbrains.anko.error
import java.io.File
import java.util.*


class ComicPageActivity : ComicPageBaseFullScreenActivity() {
    private val alertDialog: AlertDialog by lazy { AlertDialog.Builder(this).create() }
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private lateinit var presenter: ComicPagePresenter
    private lateinit var comicName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        comicName = intent.getStringExtra("comicName") ?: return
        val comicUrl = intent.getStringExtra("comicUrl") ?: return
        val issueIndex = intent.getIntExtra("issueIndex", 0)

        urlTextView.text = comicUrl
        urlBar.setOnClickListener {
            browse(urlTextView.text.toString())
        }
        loading(progressDialog, R.string.comic_page)

        // 监听器确保只添加一次，
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                hide()
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                debug { "onPageSelected: $position" }
                when (position) {
                    0 -> {
                        presenter.requestPreviousIssue()
                    }
                    viewPager.adapter.count - 1 -> {
                        presenter.requestNextIssue()

                    }
                    else -> seekBar.progress = position - 1
                }
            }
        })

        presenter = ComicPagePresenter(this, comicUrl, issueIndex)
        presenter.start()
    }

    fun showError(message: String, e: Throwable) {
        progressDialog.dismiss()
        alertError(message, e)
    }

    fun showPreviousIssue(issue: ComicIssue, pages: List<ComicPage>) {
        showComicPages(issue, pages)
        // 跳到最后一页，
        viewPager.currentItem = pages.size
    }

    fun showNextIssue(issue: ComicIssue, pages: List<ComicPage>) {
        showComicPages(issue, pages)
        // 跳到第一页，0页不是漫画，
        viewPager.currentItem = 1
    }

    fun showNoPreviousIssue() {
        (viewPager.adapter as ComicPageAdapter).noPrevious()
    }

    fun showNoNextIssue() {
        (viewPager.adapter as ComicPageAdapter).noNext()
    }

    private fun showComicPages(issue: ComicIssue, pages: List<ComicPage>) {
        title = "$comicName - ${issue.name}"
        urlTextView.text = issue.url
        progressDialog.dismiss()
        if (pages.isEmpty()) {
            alert(alertDialog, R.string.comic_not_support)
            // 无法浏览的情况显示状态栏标题栏导航栏，方便离开，
            show()
            return
        }
        viewPager.adapter = ComicPageAdapter(this, pages)
        seekBar.max = pages.size - 1
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // 这里会调用上面的onPageSelected，
                    viewPager.setCurrentItem(progress + 1, false)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}

class ComicPageAdapter(val ctx: Activity, private val pages: List<ComicPage>) : PagerAdapter(), AnkoLogger {
    private val views: LinkedList<View> = LinkedList()
    private val imgs = mutableMapOf<ComicPage, ComicImage>()
    private val firstPage: View by lazy {
        View.inflate(ctx, R.layout.comic_page_item_loading, null).apply {
            loadingTextView.setText(R.string.now_loading_previous_issue)
        }
    }
    private val lastPage: View by lazy {
        View.inflate(ctx, R.layout.comic_page_item_loading, null).apply {
            loadingTextView.setText(R.string.now_loading_next_issue)
        }
    }

    override fun isViewFromObject(view: View, obj: Any) = view === obj
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        when (position) {
            0 -> {
                container.addView(firstPage)
                return firstPage
            }
            count - 1 -> {
                container.addView(lastPage)
                return lastPage
            }
        }
        val root = if (views.isNotEmpty())
            views.pop()
        else
            View.inflate(ctx, R.layout.comic_page_item, null).apply {
                image.setOnClickListener {
                    (context as ComicPageActivity).toggle()
                }
            }
        root.progressBar.visibility = View.VISIBLE
        // 重制放大状态，
        (root.image as PinchImageView).reset()
        root.image.setImageDrawable(null)
        root.pageNumber.text = ctx.getString(R.string.page_number, position, pages.size)
        val page = pages[position - 1]
        fun setImage(comicImage: ComicImage) {
            val (realUrl, cacheableUrl) = comicImage
            ctx.glide {
                it.download(realUrl).apply(RequestOptions().signature(ObjectKey(cacheableUrl)))
                        .into(object : SimpleTarget<File>() {
                            override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                                HugeUtil.setImageUri(root.image, Uri.fromFile(resource))
                                root.progressBar.visibility = View.GONE
                            }
                        })
            }
        }
        imgs[page]?.let { comicImage ->
            setImage(comicImage)
        } ?: page.img.async().subscribe({ comicImage ->
            imgs.put(page, comicImage)
            setImage(comicImage)
        }, { e ->
            val message = "加载漫画页面失败，"
            error(message, e)
            root.progressBar.visibility = View.GONE
        })
        container.addView(root)
        return root
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any?) {
        val view = obj as View
        container.removeView(view)
        when (position) {
            0 -> {
            }
            count - 1 -> {
            }
            else -> {
                views.push(view)
            }
        }
    }

    override fun getCount() = pages.size + 2
    fun noNext() {
        lastPage.loadingTextView.setText(R.string.no_next_issue)
        lastPage.loadingProgressBar.hide()
    }

    fun noPrevious() {
        firstPage.loadingTextView.setText(R.string.no_previous_issue)
        firstPage.loadingProgressBar.hide()
    }
}