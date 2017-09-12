package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicContext
import cc.aoeiuv020.comic.api.ComicSite
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import javax.inject.Singleton

/**
 * 提供网站信息，
 * Created by AoEiuV020 on 2017.09.12-14:15:55.
 */
@Singleton
@Component(modules = arrayOf(SiteModule::class))
interface SiteComponent {
    fun getSites(): Observable<ComicSite>
}

@Module
class SiteModule {
    @Singleton
    @Provides
    fun getSites(): Observable<ComicSite> = Observable.fromIterable(ComicContext.getComicContexts().map(ComicContext::getComicSite))
}