package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicContext
import cc.aoeiuv020.comic.api.ComicSite
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.Observable

/**
 * 提供网站信息，
 * Created by AoEiuV020 on 2017.09.12-14:15:55.
 */
@Subcomponent(modules = arrayOf(SiteModule::class))
interface SiteComponent {
    fun getSites(): Observable<List<ComicSite>>
}

@Module
class SiteModule {
    @Provides
    fun getSites(): Observable<List<ComicSite>> = Observable.fromCallable {
        ComicContext.getComicContexts().map(ComicContext::getComicSite)
    }
}