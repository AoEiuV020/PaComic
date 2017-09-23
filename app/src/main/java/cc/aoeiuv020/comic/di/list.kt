package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.api.ComicListItem
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.Observable

/**
 * 提供漫画列表，
 * Created by AoEiuV020 on 2017.09.12-17:49:22.
 */
@Subcomponent(modules = arrayOf(ListModule::class))
interface ListComponent {
    fun getComicList(): Observable<List<ComicListItem>>
    fun getNextPage(): Observable<ComicGenre>
}

@Module
class ListModule(private val comicGenre: ComicGenre) {
    @Provides
    fun getComicList(): Observable<List<ComicListItem>> = Observable.fromCallable {
        ctx(comicGenre.url).getComicList(comicGenre)
    }

    @Provides
    fun getNextPage(): Observable<ComicGenre> = Observable.create { em ->
        ctx(comicGenre.url).getNextPage(comicGenre)?.let {
            em.onNext(it)
        }
        em.onComplete()
    }
}