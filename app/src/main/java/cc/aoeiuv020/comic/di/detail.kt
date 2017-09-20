package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicDetail
import cc.aoeiuv020.comic.api.ComicListItem
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.Observable

/**
 * 提供漫画详情，
 * Created by AoEiuV020 on 2017.09.12-18:09:48.
 */
@Subcomponent(modules = arrayOf(DetailModule::class))
interface DetailComponent {
    fun getComicDetail(): Observable<ComicDetail>
}

@Module
class DetailModule(private val comicListItem: ComicListItem) {
    @Provides
    fun getComicDetail(): Observable<ComicDetail> = Observable.fromCallable {
        ctx(comicListItem.url).getComicDetail(comicListItem)
    }
}