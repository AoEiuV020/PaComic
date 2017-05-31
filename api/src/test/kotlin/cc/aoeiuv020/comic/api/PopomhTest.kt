package cc.aoeiuv020.comic.api

/**
 * Created by AoEiuV020 on 17-5-30.
 */
class PopomhTest {
    @org.junit.Test
    fun classifications() {
        val site = cc.aoeiuv020.comic.api.popomh.Popomh()
        val cs = site.classificationSpiders
        cs.forEach {
            println("${it.name}, ${it.classificationUrl}")
        }
    }
}