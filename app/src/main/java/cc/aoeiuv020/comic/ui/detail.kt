package cc.aoeiuv020.comic.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cc.aoeiuv020.comic.R
import cc.aoeiuv020.comic.api.ComicDetail
import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.api.ComicListItem
import cc.aoeiuv020.comic.di.DetailModule
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_comic_detail.*
import kotlinx.android.synthetic.main.activity_comic_detail.view.*
import kotlinx.android.synthetic.main.comic_issue_item.view.*
import kotlinx.android.synthetic.main.content_comic_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity

class ComicDetailActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val comicListItem = intent.getSerializableExtra("item") as? ComicListItem ?: return

        toolbar_layout.title = comicListItem.name
        Glide.with(this@ComicDetailActivity)
                .load(comicListItem.img)
                .into(toolbar_layout.image)
        recyclerView.adapter = ComicDetailAdapter(this@ComicDetailActivity)
        recyclerView.layoutManager = LinearLayoutManager(this@ComicDetailActivity)

        val loadingDialog = loading(R.string.comic_detail)
        App.component.plus(DetailModule(comicListItem))
                .getComicDetail()
                .async()
                .subscribe { comicDetail ->
                    setDetail(comicDetail)
                    loadingDialog.dismiss()
                }
    }

    fun setDetail(detail: ComicDetail) {
        toolbar_layout.title = detail.name
        Glide.with(this@ComicDetailActivity)
                .load(detail.bigImg)
                .into(toolbar_layout.image)
        (recyclerView.adapter as ComicDetailAdapter).setDetail(detail)
    }
}

class ComicDetailAdapter(val ctx: Context)
    : RecyclerView.Adapter<ComicDetailAdapter.Holder>() {
    private lateinit var detail: ComicDetail
    private var issuesDesc = emptyList<ComicIssue>()
    override fun getItemCount() = issuesDesc.size

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val issue = issuesDesc[position]
        holder?.root?.apply {
            name.text = issue.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = Holder(View.inflate(ctx, R.layout.comic_issue_item, null))


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
