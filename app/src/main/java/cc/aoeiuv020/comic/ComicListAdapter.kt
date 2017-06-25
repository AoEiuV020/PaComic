package cc.aoeiuv020.comic

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import cc.aoeiuv020.comic.model.ComicListItemModel
import cc.aoeiuv020.comic.util.ImageUtil
import kotlinx.android.synthetic.main.comic_list_item.view.*

/**
 * Created by AoEiuV020 on 17-6-24.
 */
class ComicListAdapter(val ctx: Context, val items: List<ComicListItemModel>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: View.inflate(ctx, R.layout.comic_list_item, null)
        val comic = getItem(position)
        view.apply {
            comic_name.text = comic.name
            ImageUtil.asyncSetImageUrl(comic_icon, comic.imgUrl)
        }
        return view
    }

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = 0L

    override fun getCount() = items.size
}