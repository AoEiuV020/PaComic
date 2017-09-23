package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.api.ComicSite
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
}

@Module
class GenreModule(private val site: ComicSite) {
    @Provides
    fun getGenres(): Observable<ComicGenre> = Observable.fromCallable {
        ctx(site.baseUrl).getGenres()
    }.flatMapIterable { it }
}