package cc.aoeiuv020.comic.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.SeekBar
import cc.aoeiuv020.comic.R
import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.api.ComicPage
import cc.aoeiuv020.comic.presenter.AlertableView
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
import org.jetbrains.anko.alert
import java.io.File
import java.util.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ComicPageActivity : ComicPageBaseFullScreenActivity(), AlertableView {
    override val ctx: Context = this
    private lateinit var presenter: ComicPagePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name") ?: return
        val issue = intent.getSerializableExtra("issue") as? ComicIssue ?: return

        presenter = ComicPagePresenter(this, name, issue)

        urlBar.setOnClickListener {
            presenter.browseCurrentUrl()
        }

        presenter.start()
    }

    fun showName(name: String) {
        title = name
    }

    fun showUrl(s: String) {
        url.text = s
    }

    fun showComicPages(pages: List<ComicPage>) {
        if (pages.isEmpty()) {
            alert("浏览失败或者不支持该漫画").show()
            // 无法浏览的情况显示状态栏标题栏导航栏，方便离开，
            show()
            return
        }
        viewPager.adapter = ComicPageAdapter(this, presenter, pages)
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

class ComicPageAdapter(val ctx: Context, private val presenter: ComicPagePresenter, private val pages: List<ComicPage>) : PagerAdapter(), AnkoLogger {
    private val views: LinkedList<View> = LinkedList()
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
        presenter.resolveComicPage(page, { (img, cacheableUrl) ->
            ctx.glide()?.also {
                it.download(img).apply(RequestOptions().signature(ObjectKey(cacheableUrl)))
                        .into(object : SimpleTarget<File>() {
                            override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                                HugeUtil.setImageUri(root.image, Uri.fromFile(resource))
                                root.progressBar.visibility = View.GONE
                            }

                        })
            }
        }, { _, _ ->
            root.progressBar.visibility = View.GONE
        })
        container.addView(root)
        val w = WebView(ctx)
        w.getContentHeight()
        return root
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any?) {
        val view = obj as View
        container.removeView(view)
        views.push(view)
    }

    override fun getCount() = pages.size
}