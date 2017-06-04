package cc.aoeiuv020.comic.model

import cc.aoeiuv020.comic.api.ClassificationSpider

/**
 * Created by AoEiuV020 on 17-5-30.
 */
class ClassificationModel(cs: ClassificationSpider) {
    val name: String = cs.name
}