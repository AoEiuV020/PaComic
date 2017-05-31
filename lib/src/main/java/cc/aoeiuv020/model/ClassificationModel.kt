package cc.aoeiuv020.model

import cc.aoeiuv020.comic.api.site.ClassificationSniper

/**
 * Created by AoEiuV020 on 17-5-30.
 */
data class ClassificationModel(val name: String, val url: String) {
    constructor(cs: ClassificationSniper) : this(cs.name, cs.url)
}