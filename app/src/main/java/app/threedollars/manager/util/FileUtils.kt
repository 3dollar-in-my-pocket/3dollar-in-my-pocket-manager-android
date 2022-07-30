package app.threedollars.manager.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.AnyRes
import androidx.annotation.NonNull
import app.threedollars.manager.MainApplication
import app.threedollars.manager.R
import com.google.android.gms.common.util.IOUtils
import java.io.File
import java.io.FileOutputStream


object FileUtils {
    const val IMAGE_MAX_SIZE = 10_000

    fun getUriToDrawable(
        @NonNull context: Context,
        @AnyRes drawableId: Int
    ): Uri {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.resources.getResourcePackageName(drawableId)
                    + '/' + context.resources.getResourceTypeName(drawableId)
                    + '/' + context.resources.getResourceEntryName(drawableId)
        )
    }

    fun drawableToFile(@AnyRes drawableId: Int = R.drawable.login_back): File? {
        return uriToFile(getUriToDrawable(MainApplication.getContext(), drawableId))
    }

    fun uriToFile(uri: Uri?): File? {
        if (uri == null) {
            return null
        }

        val resolver = MainApplication.getContext().contentResolver
        val tempFile = File.createTempFile("image${System.currentTimeMillis()}", ".png")
        resolver.openInputStream(uri)?.use { stream ->
            val outputStream = FileOutputStream(tempFile)
            IOUtils.copyStream(stream, outputStream)
            outputStream.close()
        }
        return tempFile
    }

    fun getFileSize(uri: Uri?) = uriToFile(uri)?.length()?.div(1024) ?: 0

    fun isAvailable(uri: Uri?): Boolean {
        if (uri == null) {
            return false
        }

        return getFileSize(uri) < IMAGE_MAX_SIZE
    }
}