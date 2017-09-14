package cc.aoeiuv020.comic

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import cc.aoeiuv020.comic.manager.ComicManager
import cc.aoeiuv020.comic.model.SiteModel
import cc.aoeiuv020.comic.util.ImageUtil
import kotlinx.android.synthetic.main.site_list_item.view.*

/**
 * Created by AoEiuV020 on 17-6-24.
 */
class SiteListAdapter(val ctx: Context) : BaseAdapter() {
    private val sites: List<SiteModel> = ComicManager.siteManager.siteModels
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: View.inflate(ctx, R.layout.site_list_item, null)
        val site = getItem(position)
        view.apply {
            site_name.text = site.name
            ImageUtil.asyncSetImageUrl(site_logo, site.logoUrl)
        }
        return view
    }

    override fun getItem(position: Int) = sites[position]

    override fun getItemId(position: Int) = 0L

    override fun getCount() = sites.size
}