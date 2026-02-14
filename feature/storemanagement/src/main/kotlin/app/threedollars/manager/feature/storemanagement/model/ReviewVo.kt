package app.threedollars.manager.feature.storemanagement.model


internal data class ReviewVo(
    val reviewId: String = "",
    val rating: Int = 0,
    val contents: String = "",
    val images: List<Image> = listOf(),
    val writer: Writer = Writer(),
    val createdAt: String = "",
    val sticker: Sticker = Sticker(),
    val comment: Comment? = null,
    val status: String = "",
) {
    internal data class Image(
        val imageUrl: String = "",
        val width: Int? = null,
        val height: Int? = null,
    )

    internal data class Writer(
        val name: String = "",
        val medal: Medal = Medal(),
    ) {
        internal data class Medal(
            val name: String = "",
            val iconUrl: String? = null,
        )
    }

    internal data class Sticker(
        val stickerId: String = "",
        val emoji: String = "",
        val count: Int = 0,
        val reactedByMe: Boolean = false,
    )

    internal data class Comment(
        val commentId: String = "",
        val content: String = "",
        val status: String = "",
        val isOwner: Boolean = false,
        val createdAt: String? = null,
        val updatedAt: String? = null,
    )
}
