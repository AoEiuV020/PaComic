package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.api.ComicSite
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.Observable

/**
 * 提供漫画搜索结果，
 * Created by AoEiuV020 on 2017.09.17-16:53:14.
 */
@Subcomponent(modules = arrayOf(SearchModule::class))
interface SearchComponent {
    fun search(): Observable<ComicGenre>
}

@Module
class SearchModule(private val site: ComicSite, private var name: String) {
    @Provides
    fun search(): Observable<ComicGenre> = Observable.create { em ->
        ctx(site.baseUrl).search(name).let {
            em.onNext(it)
        }
        em.onComplete()
    }
}