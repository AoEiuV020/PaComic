package cc.aoeiuv020.comic.api.site

import org.junit.Test

/**
 * Created by AoEiuV020 on 17-5-30.
 */
class PopomhTest {
    @Test
    fun classifications() {
        val site = Popomh()
        val cs = site.classificationSnipers
        cs.forEach(::println)
    }
}