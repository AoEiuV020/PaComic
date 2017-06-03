package cc.aoeiuv020.comic.api.popomh

import cc.aoeiuv020.comic.api.ComicItemSpider

class PopomhComicItemSpider(popomh: Popomh,
                            override val name: String,
                            override val imgUrl: String,
                            override val comicDetailUrl: String) : ComicItemSpider() {
    override val comicDetail: PopomhComicDetailSpider by lazy {
        PopomhComicDetailSpider(popomh, comicDetailUrl, name)
    }
}