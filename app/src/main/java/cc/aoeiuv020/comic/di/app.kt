package cc.aoeiuv020.comic.di

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * 提供全局context,
 * Created by AoEiuV020 on 2017.09.13-18:28:38.
 */
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    val ctx: Context

    fun plus(module: SiteModule): SiteComponent
    fun plus(module: GenreModule): GenreComponent
    fun plus(module: ListModule): ListComponent
    fun plus(module: DetailModule): DetailComponent
    fun plus(module: PageModule): PageComponent
    fun plus(module: ImageModule): ImageComponent
}

@Module
class AppModule(val ctx: Context) {
    @Provides
    fun ctx() = ctx
}