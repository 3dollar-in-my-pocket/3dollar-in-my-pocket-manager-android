package app.threedollars.common.ext

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import app.threedollars.common.R

fun Context.toast(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.getInstalledInfo() =
    """
        -------------------------------------------------------
        ${getString(R.string.app_name)}
        ${getString(R.string.android_version)}: ${Build.VERSION.SDK_INT}
        ------------------------------------------------------- 
    """.trimIndent()
