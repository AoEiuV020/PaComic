package cc.aoeiuv020.data

import cc.aoeiuv020.comic.api.site.ClassificationSpider
import cc.aoeiuv020.model.ClassificationModel

/**
 * 处理分类这块的爬虫和模型，
 * 有的方法涉及网络访问，
 */
class ClassificationManager {
    internal val classificationSpiders: List<ClassificationSpider>?
        get() = ApiManager.siteManager.siteSpider?.classificationSpiders
    internal val classificationSpider: ClassificationSpider?
        get() = classificationIndex?.let { i -> classificationSpiders?.let { s -> s[i] } }
    val classificationModel: ClassificationModel?
        get() = classificationSpider?.let { ClassificationModel(it) }
    var classificationIndex: Int? = null
    val classificationModels: List<ClassificationModel>?
        get() = classificationSpiders?.map { ClassificationModel(it) }

    fun reset() {
        classificationIndex = null
    }
}