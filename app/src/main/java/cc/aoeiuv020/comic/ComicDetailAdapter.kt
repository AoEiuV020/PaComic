package cc.aoeiuv020.comic

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cc.aoeiuv020.comic.manager.ComicManager
import cc.aoeiuv020.comic.model.ComicContentsModel
import kotlinx.android.synthetic.main.comic_content_item.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity

/**
 * Created by AoEiuV020 on 17-6-24.
 */
class ComicDetailAdapter(val ctx: Context, val contents: List<ComicContentsModel>)
    : RecyclerView.Adapter<ComicDetailAdapter.Holder>() {
    override fun getItemCount() = contents.size

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val content = contents[position]
        holder?.root?.apply {
            name.text = content.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = Holder(View.inflate(ctx, R.layout.comic_content_item, null))

    class Holder(val root: View) : RecyclerView.ViewHolder(root), AnkoLogger {
        init {
            root.setOnClickListener {
                ComicManager.comicContentsManager.comicContentsIndex = layoutPosition
                root.context.startActivity<ComicPageActivity>()
            }
        }
    }
}