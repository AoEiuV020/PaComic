package cc.aoeiuv020.comic.ui

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import cc.aoeiuv020.comic.R
import cc.aoeiuv020.comic.api.ComicDetail
import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.api.ComicListItem
import cc.aoeiuv020.comic.presenter.ComicDetailPresenter
import kotlinx.android.synthetic.main.activity_comic_detail.*
import kotlinx.android.synthetic.main.activity_comic_detail.view.*
import kotlinx.android.synthetic.main.comic_issue_item.view.*
import kotlinx.android.synthetic.main.content_comic_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.browse
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity

class ComicDetailActivity : AppCompatActivity(), AnkoLogger {
    private val alertDialog: AlertDialog by lazy { AlertDialog.Builder(this).create() }
    @Suppress("DEPRECATION")
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private lateinit var url: String
    private lateinit var presenter: ComicDetailPresenter
    private var comicDetail: ComicDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val comicListItem = intent.getSerializableExtra("item") as? ComicListItem ?: run {
            error { "ComicDetailActivity 没能从intent得到ComicListItem, 打开方式不对吧，" }
            finish()
            return
        }
        url = comicListItem.url

        recyclerView.adapter = ComicDetailAdapter(this@ComicDetailActivity)
        recyclerView.layoutManager = LinearLayoutManager(this@ComicDetailActivity)

        loading(progressDialog, R.string.comic_detail)
        showGeneral(comicListItem)

        presenter = ComicDetailPresenter(this, comicListItem)
        presenter.start()
    }

    private fun showGeneral(comicListItem: ComicListItem) {
        toolbar_layout.title = comicListItem.name
        glide()?.also {
            it.load(comicListItem.img).into(toolbar_layout.image)
        }
    }

    fun showComicDetail(detail: ComicDetail) {
        this.comicDetail = detail
        progressDialog.dismiss()
        toolbar_layout.title = detail.name
        glide()?.also {
            it.load(detail.bigImg).holdInto(toolbar_layout.image)
        }
        (recyclerView.adapter as ComicDetailAdapter).setDetail(detail)
    }

    fun showError(message: String, e: Throwable) {
        progressDialog.dismiss()
        alertError(message, e)
    }

    private fun showComicAbout() {
        comicDetail?.let {
            alert(alertDialog, it.name, it.info)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        menu.findItem(R.id.browse).setOnMenuItemClickListener {
            browse(url)
        }
        menu.findItem(R.id.info).setOnMenuItemClickListener {
            showComicAbout()
            true
        }
        return true
    }
}

class ComicDetailAdapter(val ctx: Context) : RecyclerView.Adapter<ComicDetailAdapter.Holder>() {
    private lateinit var detail: ComicDetail
    private var issuesDesc = emptyList<ComicIssue>()
    override fun getItemCount() = issuesDesc.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val issue = issuesDesc[position]
        holder.root.apply {
            name.text = issue.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = Holder(LayoutInflater.from(ctx).inflate(R.layout.comic_issue_item, parent, false))

    fun setDetail(detail: ComicDetail) {
        this.detail = detail
        issuesDesc = detail.issuesAsc.asReversed()
        notifyDataSetChanged()
    }

    inner class Holder(val root: View) : RecyclerView.ViewHolder(root), AnkoLogger {
        init {
            root.setOnClickListener {
                ctx.startActivity<ComicPageActivity>("name" to detail.name, "issue" to issuesDesc[layoutPosition])
            }
        }
    }
}
