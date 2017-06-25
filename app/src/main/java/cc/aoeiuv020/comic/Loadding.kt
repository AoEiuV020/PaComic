package cc.aoeiuv020.comic

import android.content.Context
import org.jetbrains.anko.indeterminateProgressDialog

/**
 * Created by AoEiuV020 on 17-6-24.
 */
fun Context.loading() = indeterminateProgressDialog(R.string.loading)