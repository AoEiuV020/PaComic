package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.api.ComicSite
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import javax.inject.Singleton

/**
 * 提供漫画分类信息，
 * Created by AoEiuV020 on 2017.09.12-15:21:09.
 */
@Singleton
@Component(modules = arrayOf(GenreModule::class))
interface GenreComponent {
    fun getGenre(): Observable<ComicGenre>
}

@Module
class GenreModule(val site: ComicSite) {
    @Provides
    fun getGenre(): Observable<ComicGenre> = Observable.create { em ->
        ctx(site.baseUrl).getGenres().forEach {
            em.onNext(it)
        }
        em.onComplete()
    }
}