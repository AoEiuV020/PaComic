package cc.aoeiuv020.comic.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import cc.aoeiuv020.comic.R
import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.api.ComicPage
import cc.aoeiuv020.comic.di.ImageModule
import cc.aoeiuv020.comic.di.PageModule
import cc.aoeiuv020.comic.ui.base.ComicPageBaseFullScreenActivity
import com.boycy815.pinchimageview.PinchImageView
import com.boycy815.pinchimageview.huge.HugeUtil
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_comic_page.*
import kotlinx.android.synthetic.main.comic_page_item.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.browse
import org.jetbrains.anko.error
import java.io.File
import java.util.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ComicPageActivity : ComicPageBaseFullScreenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name") ?: return
        val issue = intent.getSerializableExtra("issue") as? ComicIssue ?: return
        val loadingDialog = loading(R.string.comic_page)
        title = "$name - ${issue.name}"
        urlBar.setOnClickListener {
            browse(url.text.toString())
        }
        // 显示第一页地址，
        url.text = issue.url
        App.component.plus(PageModule(issue))
                .getComicPages()
                .async()
                .toList()
                .subscribe({ pages ->
                    display(pages)
                    loadingDialog.dismiss()
                }, { e ->
                    val message = "加载漫画页面失败，"
                    error(message, e)
                    alertError(message, e)
                    loadingDialog.dismiss()
                })
    }

    private fun display(pages: List<ComicPage>) {
        if (pages.isEmpty()) {
            alert("浏览失败或者不支持该漫画").show()
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
                url.text = (viewPager.adapter as ComicPageAdapter).getItem(viewPager.currentItem).url
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
    private val imgs = SparseArray<String>()
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
        imgs[position]?.let { img ->
            ctx.glide()?.also {
                it.download(img).into(object : SimpleTarget<File>() {
                    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                        HugeUtil.setImageUri(root.image, Uri.fromFile(resource))
                        root.progressBar.visibility = View.GONE
                    }

                })
            }
        } ?: run {
            App.component.plus(ImageModule(page))
                    .getComicImage()
                    .async()
                    .subscribe({ (img) ->
                        imgs.put(position, img)
                        ctx.glide()?.also {
                            it.download(img).into(object : SimpleTarget<File>() {
                                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                                    HugeUtil.setImageUri(root.image, Uri.fromFile(resource))
                                    root.progressBar.visibility = View.GONE
                                }

                            })
                        }
                    }, { e ->
                        val message = "加载漫画页面失败，"
                        error(message, e)
                        root.progressBar.visibility = View.GONE
                    })
        }
        container.addView(root)
        return root
    }

    fun getItem(position: Int): ComicPage = pages[position]

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any?) {
        val view = obj as View
        container.removeView(view)
        views.push(view)
    }

    override fun getCount() = pages.size
}