package app.threedollars.data.response

import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.model.CursorModel
import app.threedollars.domain.dto.StorePostDto
import app.threedollars.domain.dto.StorePostListDto
import app.threedollars.domain.dto.StorePostSectionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StorePostListResponse(
    @SerialName("contents")
    val contents: List<StorePostResponse> = listOf(),
    @SerialName("cursor")
    val cursor: CursorModel? = CursorModel(),
) {
    fun toDto() = StorePostListDto(
        contents = contents.map { it.toDto() },
        cursor = cursor?.toDto(),
    )
}

@Serializable
internal data class StorePostResponse(
    @SerialName("postId")
    val postId: String = "",
    @SerialName("body")
    val body: String? = null,
    @SerialName("sections")
    val sections: List<Section> = listOf(),
    @SerialName("isOwner")
    val isOwner: Boolean = false,
    @SerialName("store")
    val store: Store? = null,
    @SerialName("stickers")
    val stickers: List<Sticker> = listOf(),
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
) {
    @Serializable
    internal data class Section(
        @SerialName("sectionType")
        val sectionType: String? = null,
        @SerialName("url")
        val url: String? = null,
        @SerialName("ratio")
        val ratio: Double? = null,
    ) {
        fun toDto() = StorePostDto.Section(
            sectionType = StorePostSectionType.from(sectionType),
            url = url.toStringDefault(),
            ratio = ratio ?: 1.0,
        )
    }

    @Serializable
    internal data class Store(
        @SerialName("storeName")
        val storeName: String? = null,
        @SerialName("categories")
        val categories: List<Category> = listOf(),
    ) {
        @Serializable
        internal data class Category(
            @SerialName("imageUrl")
            val imageUrl: String? = null,
        )

        fun toDto() = StorePostDto.Store(
            storeName = storeName.toStringDefault(),
            categories = categories.map { StorePostDto.Store.Category(imageUrl = it.imageUrl.toStringDefault()) },
        )
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
        val reactedByMe: Boolean = false,
    ) {
        fun toDto() = StorePostDto.Sticker(
            stickerId = stickerId,
            emoji = emoji,
            count = count,
            reactedByMe = reactedByMe,
        )
    }

    fun toDto() = StorePostDto(
        postId = postId,
        body = body.toStringDefault(),
        sections = sections.map { it.toDto() },
        isOwner = isOwner,
        store = store?.toDto() ?: StorePostDto.Store(),
        stickers = stickers.map { it.toDto() },
        createdAt = createdAt.toStringDefault(),
        updatedAt = updatedAt.toStringDefault(),
    )
}

@Serializable
internal data class StorePostCreateResponse(
    @SerialName("postId")
    val postId: String = "",
)
