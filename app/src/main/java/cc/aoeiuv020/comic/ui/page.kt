package cc.aoeiuv020.comic.ui

import android.app.ProgressDialog
import android.content.Context
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
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.browse
import org.jetbrains.anko.error
import java.io.File
import java.util.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ComicPageActivity : ComicPageBaseFullScreenActivity() {
    private val alertDialog: AlertDialog by lazy { AlertDialog.Builder(this).create() }
    @Suppress("DEPRECATION")
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private lateinit var url: String
    private lateinit var presenter: ComicPagePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name") ?: return
        val issue = intent.getSerializableExtra("issue") as? ComicIssue ?: return
        url = issue.url


        urlBar.setOnClickListener {
            browse(url)
        }

        showName("$name - ${issue.name}")
        loading(progressDialog, R.string.comic_page)

        presenter = ComicPagePresenter(this, issue)
        presenter.start()
    }

    private fun showName(name: String) {
        title = name
    }

    fun showError(message: String, e: Throwable) {
        progressDialog.dismiss()
        alertError(message, e)
    }

    fun showComicPages(pages: List<ComicPage>) {
        progressDialog.dismiss()
        if (pages.isEmpty()) {
            alert(alertDialog, R.string.comic_not_support)
            // 无法浏览的情况显示状态栏标题栏导航栏，方便离开，
            show()
            return
        }
        viewPager.adapter = ComicPageAdapter(this, pages)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                hide()
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                seekBar.progress = position
            }
        })
        seekBar.max = pages.size - 1
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // 这里会调用上面的onPageSelected，
                    viewPager.setCurrentItem(progress, false)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}

class ComicPageAdapter(val ctx: Context, private val pages: List<ComicPage>) : PagerAdapter(), AnkoLogger {
    private val views: LinkedList<View> = LinkedList()
    private val imgs = mutableMapOf<ComicPage, ComicImage>()
    override fun isViewFromObject(view: View, obj: Any) = view === obj
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
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
        root.pageNumber.text = ctx.getString(R.string.page_number, position + 1, count)
        val page = pages[position]
        fun setImage(comicImage: ComicImage) {
            val (img, cacheableUrl) = comicImage
            ctx.glide {
                it.download(img).apply(RequestOptions().signature(ObjectKey(cacheableUrl)))
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
        } ?: page.url.async().subscribe({ comicImage ->
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
        views.push(view)
    }

    override fun getCount() = pages.size
}