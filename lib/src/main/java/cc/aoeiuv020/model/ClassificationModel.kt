package cc.aoeiuv020.model

import cc.aoeiuv020.comic.api.site.ClassificationSpider

/**
 * Created by AoEiuV020 on 17-5-30.
 */
class ClassificationModel(cs: ClassificationSpider) {
    val name: String = cs.name
}