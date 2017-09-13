package cc.aoeiuv020.comic.di

import android.content.Context
import cc.aoeiuv020.comic.api.ComicContext
import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.api.ComicSite
import cc.aoeiuv020.comic.ui.App
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.Observable

/**
 * 提供漫画分类信息，
 * Created by AoEiuV020 on 2017.09.12-15:21:09.
 */
@Subcomponent(modules = arrayOf(GenreModule::class))
interface GenreComponent {
    fun getGenres(): Observable<ComicGenre>
    /**
     * 提供记住了的选择，
     */
    val genre: ComicGenre?
}

@Module
class GenreModule(val site: ComicSite) {
    init {
        App.component.ctx.getSharedPreferences("site", Context.MODE_PRIVATE)
                .edit()
                .putString("baseUrl", site.baseUrl)
                .apply()
    }

    @Provides
    fun getGenres(): Observable<ComicGenre> = Observable.create { em ->
        ctx(site.baseUrl).getGenres().forEach {
            em.onNext(it)
        }
        em.onComplete()
    }

    @Provides
    fun genre(): ComicGenre? {
        return App.component.ctx.getSharedPreferences("genre", Context.MODE_PRIVATE).run {
            val url = getString("url", null) ?: return null
            val name = getString("name", "")
            // 仅当url属于这个site,
            ComicGenre(name, url).takeIf { ComicContext.getComicContext(site.baseUrl)!!.check(url) }
        }
    }
}