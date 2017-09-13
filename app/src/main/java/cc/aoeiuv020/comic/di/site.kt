package cc.aoeiuv020.comic.di

import android.content.Context
import cc.aoeiuv020.comic.api.ComicContext
import cc.aoeiuv020.comic.api.ComicSite
import cc.aoeiuv020.comic.ui.App
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
    fun getSites(): Observable<ComicSite>
    val site: ComicSite?
}

@Module
class SiteModule {
    @Provides
    fun getSites(): Observable<ComicSite> = Observable.fromIterable(ComicContext.getComicContexts().map(ComicContext::getComicSite))

    @Provides
    fun site(): ComicSite? {
        val baseUrl = App.component.ctx.getSharedPreferences("site", Context.MODE_PRIVATE)
                .getString("baseUrl", "")
        return ComicContext.getComicContext(baseUrl)?.getComicSite()
    }
}