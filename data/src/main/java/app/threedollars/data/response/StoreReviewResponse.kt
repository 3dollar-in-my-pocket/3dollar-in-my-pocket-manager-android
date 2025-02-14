package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.data.model.CursorModel
import app.threedollars.domain.dto.StoreReviewDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StoreReviewResponse(
    @SerialName("contents")
    val contents: List<StoreReview> = listOf(),
    @SerialName("cursor")
    val cursor: CursorModel? = CursorModel(),
) : BaseResponse<StoreReviewResponse>() {

    @Serializable
    internal data class StoreReview(
        @SerialName("reviewId")
        val reviewId: String = "",
        @SerialName("rating")
        val rating: Int = 0,
        @SerialName("contents")
        val contents: String?,
        @SerialName("images")
        val images: List<Image> = listOf(),
        @SerialName("status")
        val status: String = "",
        @SerialName("writer")
        val writer: Writer = Writer(),
        @SerialName("stickers")
        val stickers: List<Sticker> = listOf(),
        @SerialName("comments")
        val comments: Comments = Comments(),
        @SerialName("createdAt")
        val createdAt: String? = null,
        @SerialName("updatedAt")
        val updatedAt: String? = null
    ) {
        @Serializable
        internal data class Image(
            @SerialName("imageUrl")
            val imageUrl: String = "",
            @SerialName("width")
            val width: Int? = null,
            @SerialName("height")
            val height: Int? = null
        ) {
            fun toDto() = StoreReviewDto.StoreReview.Image(
                imageUrl = imageUrl,
                width = width,
                height = height
            )
        }

        @Serializable
        internal data class Writer(
            @SerialName("userId")
            val userId: Int? = null,
            @SerialName("name")
            val name: String = "",
            @SerialName("socialType")
            val socialType: String? = null,
            @SerialName("medal")
            val medal: Medal = Medal()
        ) {
            fun toDto() = StoreReviewDto.StoreReview.Writer(
                userId = userId,
                name = name,
                socialType = socialType,
                medal = medal.toDto()
            )

            @Serializable
            internal data class Medal(
                @SerialName("medalId")
                val medalId: Int? = null,
                @SerialName("name")
                val name: String = "",
                @SerialName("iconUrl")
                val iconUrl: String? = null,
                @SerialName("disableIconUrl")
                val disableIconUrl: String? = null,
                @SerialName("createdAt")
                val createdAt: String? = null,
                @SerialName("updatedAt")
                val updatedAt: String? = null
            ) {
                fun toDto() = StoreReviewDto.StoreReview.Writer.Medal(
                    medalId = medalId,
                    name = name,
                    iconUrl = iconUrl,
                    disableIconUrl = disableIconUrl,
                    createdAt = createdAt,
                    updatedAt = updatedAt
                )
            }
        }

        @Serializable
        internal data class Sticker(
            @SerialName("stickerId")
            val stickerId: String = "",
            @SerialName("emoji")
            val emoji: String = "",
            @SerialName("count")
            val count: Int = 0,
            @SerialName("reactedByMe")
            val reactedByMe: Boolean = false
        ) {
            fun toDto() = StoreReviewDto.StoreReview.Sticker(
                stickerId = stickerId,
                emoji = emoji,
                count = count,
                reactedByMe = reactedByMe
            )
        }

        @Serializable
        internal data class Comments(
            @SerialName("contents")
            val contents : List<Contents> = listOf()
        ) {
            @Serializable
            internal data class Contents(
                @SerialName("commentId")
                val commentId: String = "",
                @SerialName("content")
                val content: String = "",
                @SerialName("status")
                val status: String = "",
                @SerialName("isOwner")
                val isOwner: Boolean = false,
                @SerialName("createdAt")
                val createdAt: String? = null,
                @SerialName("updatedAt")
                val updatedAt: String? = null
            ) {
                fun toDto() = StoreReviewDto.StoreReview.Comment(
                    commentId = commentId,
                    content = content,
                    status = status,
                    isOwner = isOwner,
                    createdAt = createdAt,
                    updatedAt = updatedAt
                )
            }
        }

        fun toDto() = StoreReviewDto.StoreReview(
            reviewId = reviewId,
            rating = rating,
            contents = contents,
            images = images.map { it.toDto() },
            status = status,
            writer = writer.toDto(),
            stickers = stickers.map { it.toDto() },
            comments = comments.contents.map { it.toDto() },
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    fun toDto() = StoreReviewDto(
        contents = contents.filter { it.status == "POSTED" }.map { it.toDto() },
        cursor = cursor?.toDto()
    )
}