package cc.aoeiuv020.comic.manager

/**
 * 所有行为都不要在ui线程调用，是否耗时不一定，有点混乱，
 * Created by AoEiuV020 on 17-5-30.
 */

object ComicManager {
    val siteManager = SiteManager()
    val classificationManager = ClassificationManager()
    val comicListManager = ComicListManager()
    val comicDetailManager = ComicDetailManager()
    val comicContentsManager = ComicContentsManager()
    val comicPageManager = ComicPageManager()
    val comicSearchManager = ComicSearchManager()
}

