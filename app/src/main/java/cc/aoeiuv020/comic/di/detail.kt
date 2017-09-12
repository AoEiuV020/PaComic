package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicDetail
import cc.aoeiuv020.comic.api.ComicListItem
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import javax.inject.Singleton

/**
 * 提供漫画详情，
 * Created by AoEiuV020 on 2017.09.12-18:09:48.
 */
@Singleton
@Component(modules = arrayOf(DetailModule::class))
interface DetailComponent {
    fun getComicDetail(): Observable<ComicDetail>
}

@Module
class DetailModule(val comicListItem: ComicListItem) {
    @Provides
    fun getComicDetail(): Observable<ComicDetail>
            = Observable.just(ctx(comicListItem.url).getComicDetail(comicListItem))
}