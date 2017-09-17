package cc.aoeiuv020.comic.ui

import android.content.Context
import android.os.Bundle
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
import cc.aoeiuv020.comic.di.DetailModule
import kotlinx.android.synthetic.main.activity_comic_detail.*
import kotlinx.android.synthetic.main.activity_comic_detail.view.*
import kotlinx.android.synthetic.main.comic_issue_item.view.*
import kotlinx.android.synthetic.main.content_comic_detail.*
import org.jetbrains.anko.*

class ComicDetailActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var comicListItem: ComicListItem
    private var comicDetail: ComicDetail? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        comicListItem = intent.getSerializableExtra("item") as? ComicListItem ?: run {
            error { "ComicDetailActivity 没能从intent得到ComicListItem, 打开方式不对吧，" }
            finish()
            return
        }

        toolbar_layout.title = comicListItem.name
        ctx.glide()?.also {
            it.load(comicListItem.img).into(toolbar_layout.image)
        }
        recyclerView.adapter = ComicDetailAdapter(this@ComicDetailActivity)
        recyclerView.layoutManager = LinearLayoutManager(this@ComicDetailActivity)

        val loadingDialog = loading(R.string.comic_detail)
        App.component.plus(DetailModule(comicListItem))
                .getComicDetail()
                .async()
                .subscribe({ comicDetail ->
                    setDetail(comicDetail)
                    loadingDialog.dismiss()
                }, { e ->
                    val message = "加载漫画详情失败，"
                    error(message, e)
                    alertError(message, e)
                    loadingDialog.dismiss()
                })
    }

    private fun setDetail(detail: ComicDetail) {
        this.comicDetail = detail
        toolbar_layout.title = detail.name
        ctx.glide()?.also {
            it.load(detail.bigImg).holdInto(toolbar_layout.image)
        }
        (recyclerView.adapter as ComicDetailAdapter).setDetail(detail)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        menu.findItem(R.id.browse).setOnMenuItemClickListener {
            browse(comicListItem.url)
        }
        menu.findItem(R.id.info).setOnMenuItemClickListener {
            // 这个comicDetail本质上是调用getter，不是直接传对象到内部类，
            comicDetail?.let {
                alert(it.info, it.name).show()
            }
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
