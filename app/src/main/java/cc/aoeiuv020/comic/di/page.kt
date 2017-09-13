package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.api.ComicPage
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.Observable

/**
 * 提供漫画图片页面，
 * Created by AoEiuV020 on 2017.09.12-18:09:59.
 */
@Subcomponent(modules = arrayOf(PageModule::class))
interface PageComponent {
    fun getComicPages(): Observable<ComicPage>
}

@Module
class PageModule(val comicIssue: ComicIssue) {
    @Provides
    fun getComicPages(): Observable<ComicPage> = Observable.create { em ->
        ctx(comicIssue.url).getComicPages(comicIssue).forEach {
            em.onNext(it)
        }
        em.onComplete()
    }
}