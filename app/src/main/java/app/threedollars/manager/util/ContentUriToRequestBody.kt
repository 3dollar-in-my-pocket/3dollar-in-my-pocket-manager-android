package app.threedollars.manager.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

class ContentUriToRequestBody(context: Context, private val uri: Uri) : RequestBody() {

    private val contentResolver = context.contentResolver

    private var fileName = ""
    private var size = -1L

    init {
        contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
            }
        }
    }

    private fun getFileName() = fileName

    override fun contentLength(): Long = size

    override fun contentType(): MediaType? =
        contentResolver.getType(uri)?.toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        contentResolver.openInputStream(uri)?.source()?.use { source ->
            sink.writeAll(source)
        }
    }

}