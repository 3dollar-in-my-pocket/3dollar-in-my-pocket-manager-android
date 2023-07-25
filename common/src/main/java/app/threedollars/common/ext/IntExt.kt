package app.threedollars.common.ext

import android.content.ContentResolver
import android.content.Context
import android.net.Uri

fun Int.getResourceUri(context: Context): Uri {
    return context.resources.let {
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(it.getResourcePackageName(this))        // it : resources, this : ResId(Int)
            .appendPath(it.getResourceTypeName(this))        // it : resources, this : ResId(Int)
            .appendPath(it.getResourceEntryName(this))        // it : resources, this : ResId(Int)
            .build()
    }
}