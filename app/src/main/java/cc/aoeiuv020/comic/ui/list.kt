@file:Suppress("DEPRECATION")

package cc.aoeiuv020.comic.ui

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import cc.aoeiuv020.comic.R
import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.api.ComicListItem
import cc.aoeiuv020.comic.presenter.ComicListPresenter
import kotlinx.android.synthetic.main.comic_list_item.view.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor

/**
 * 展示漫画列表，
 * Created by AoEiuV020 on 2017.09.23-22:38:56.
 */
class ComicListFragment : Fragment() {
    private val alertDialog: AlertDialog by lazy { AlertDialog.Builder(context).create() }
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(context) }
    private lateinit var presenter: ComicListPresenter
    private var isEnd = false
    private var isLoadingNextPage = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.content_main, container, false)
        presenter = ComicListPresenter(this)
        return root
    }

    fun showError(message: String, e: Throwable) {
        progressDialog.dismiss()
        context.alertError(alertDialog, message, e)
    }

    fun showComicList(comicList: List<ComicListItem>) {
        isLoadingNextPage = false
        progressDialog.dismiss()
        listView.run {
            adapter = ComicListAdapter(activity, comicList)
            setOnItemClickListener { _, view, position, _ ->
                val item = adapter.getItem(position) as ComicListItem
                val intent = context.intentFor<ComicDetailActivity>("comicUrl" to item.detailUrl.url,
                        "comicName" to item.name,
                        "comicIcon" to view.comic_icon.getTag(R.id.comic_icon))
                val options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, view.comic_icon, "image")
                startActivity(intent, options.toBundle())
            }
            setOnScrollListener(object : AbsListView.OnScrollListener {
                private var lastItem = 0

                override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                    // 求画面上最后一个的索引，并不准，可能是最后一个+1,
                    lastItem = firstVisibleItem + visibleItemCount
                }

                override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                    // 差不多就好，反正没到底也快了，
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && lastItem >= adapter.count - 2) {
                        if (isLoadingNextPage || isEnd) {
                            return
                        }
                        isLoadingNextPage = true
                        context.loading(progressDialog, R.string.next_page)
                        presenter.loadNextPage()
                    }
                }
            })
        }
    }

    fun addComicList(comicList: List<ComicListItem>) {
        isLoadingNextPage = false
        progressDialog.dismiss()
        if (listView.adapter != null) {
            (listView.adapter as ComicListAdapter).addAll(comicList)
        } else {
            showComicList(comicList)
        }
    }

    fun showYetLastPage() {
        isEnd = true
        isLoadingNextPage = false
        progressDialog.dismiss()
        context.alert(alertDialog, R.string.yet_last_page)
    }

    fun showGenre(genre: ComicGenre) {
        isEnd = false
        isLoadingNextPage = false
        context.loading(progressDialog, R.string.comic_list)
        presenter.requestComicList(genre)
    }

    fun showUrl(url: String) {
        (activity as MainActivity).showUrl(url)
    }
}

class ComicListAdapter(val ctx: Activity, data: List<ComicListItem>) : BaseAdapter(), AnkoLogger {
    private val items = data.toMutableList()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View
            = (convertView ?: View.inflate(ctx, R.layout.comic_list_item, null)).apply {
        val comic = getItem(position)
        comic_name.text = comic.name
        comic_info.text = comic.info
        comic_icon.setImageDrawable(null)
        comic.img.async().subscribe { (img) ->
            comic_icon.setTag(R.id.comic_icon, img)
            ctx.glide {
                it.load(img).into(comic_icon)
            }
        }
    }

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = 0L

    override fun getCount() = items.size
    fun addAll(comicList: List<ComicListItem>) {
        items.addAll(comicList)
        notifyDataSetChanged()
    }
}