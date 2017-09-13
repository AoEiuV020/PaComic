package cc.aoeiuv020.comic.ui

import android.app.Application
import cc.aoeiuv020.comic.di.AppComponent
import cc.aoeiuv020.comic.di.AppModule
import cc.aoeiuv020.comic.di.DaggerAppComponent

/**
 * 在这里初始化一些东西，
 * Created by AoEiuV020 on 2017.09.13-18:27:20.
 */
class App : Application() {
    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        App.component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}
