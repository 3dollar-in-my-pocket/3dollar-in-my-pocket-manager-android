package app.threedollars.domain.dto


data class StoreReviewDto(
    val contents: List<StoreReview> = listOf(),
    val cursor: CursorDto? = CursorDto(),
) {

    data class StoreReview(
        val reviewId: String = "",
        val rating: Int = 0,
        val contents: String?,
        val images: List<Image> = listOf(),
        val status: String = "",
        val writer: Writer = Writer(),
        val stickers: List<Sticker> = listOf(),
        val comments: List<Comment> = listOf(),
        val createdAt: String? = null,
        val updatedAt: String? = null
    ) {
        data class Image(
            val imageUrl: String = "",
            val width: Int? = null,
            val height: Int? = null
        )

        data class Writer(
            val userId: Int? = null,
            val name: String = "",
            val socialType: String? = null,
            val medal: Medal = Medal()
        ) {
            data class Medal(
                val medalId: Int? = null,
                val name: String = "",
                val iconUrl: String? = null,
                val disableIconUrl: String? = null,
                val createdAt: String? = null,
                val updatedAt: String? = null
            )
        }

        data class Sticker(
            val stickerId: String = "",
            val emoji: String = "",
            val count: Int = 0,
            val reactedByMe: Boolean = false
        )

        data class Comment(
            val commentId: String = "",
            val content: String = "",
            val status: String = "",
            val isOwner: Boolean = false,
            val createdAt: String? = null,
            val updatedAt: String? = null
        )
    }
}