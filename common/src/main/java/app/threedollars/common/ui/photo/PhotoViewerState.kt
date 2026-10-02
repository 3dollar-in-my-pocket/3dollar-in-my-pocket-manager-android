package app.threedollars.common.ui.photo

const val PHOTO_VIEWER_MIN_SCALE = 1f
const val PHOTO_VIEWER_MAX_SCALE = 4f

data class PhotoViewerPosition(
    val index: Int,
    val total: Int,
) {
    val displayIndex: Int get() = index + 1
    val isPreviousVisible: Boolean get() = index > 0
    val isNextVisible: Boolean get() = index < total - 1

    companion object {
        fun of(initialIndex: Int, total: Int): PhotoViewerPosition =
            PhotoViewerPosition(index = initialIndex.coerceIn(0, (total - 1).coerceAtLeast(0)), total = total)
    }
}

fun clampPhotoScale(scale: Float): Float = scale.coerceIn(PHOTO_VIEWER_MIN_SCALE, PHOTO_VIEWER_MAX_SCALE)

fun clampPhotoOffset(offset: Float, scale: Float, size: Float): Float {
    val max = ((scale - 1f) * size / 2f).coerceAtLeast(0f)
    return offset.coerceIn(-max, max)
}
