package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.ComicImage
import cc.aoeiuv020.comic.api.ComicPage
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import javax.inject.Singleton

/**
 * 提供漫画图片，
 * Created by AoEiuV020 on 2017.09.12-18:10:07.
 */
@Singleton
@Component(modules = arrayOf(ImageModule::class))
interface ImageComponent {
    fun getComicImage(): Observable<ComicImage>
}

@Module
class ImageModule(val comicPage: ComicPage) {
    @Provides
    fun getComicImage(): Observable<ComicImage> = Observable.create { em ->
        em.onNext(ctx(comicPage.url).getComicImage(comicPage))
        em.onComplete()
    }
}