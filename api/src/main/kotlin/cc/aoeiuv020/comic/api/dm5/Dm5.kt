package cc.aoeiuv020.comic.api.dm5

import cc.aoeiuv020.comic.api.ClassificationSpider
import cc.aoeiuv020.comic.api.SiteSpider

/**
 * Created by AoEiuV020 on 17-6-3.
 */

class Dm5 : SiteSpider() {
    override val name = "动漫屋"
    override val home = "http://www.dm5.com"
    override val logoUrl = "http://js16.tel.cdndm.com/v201704261735/default/images/newImages/index_main_logo.png"
    override val classificationSpiders: List<ClassificationSpider>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun search(name: String): Dm5SearchResultSpider = Dm5SearchResultSpider(this, name)
}
