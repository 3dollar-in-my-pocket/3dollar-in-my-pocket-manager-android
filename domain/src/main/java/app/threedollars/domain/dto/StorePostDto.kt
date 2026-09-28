package app.threedollars.domain.dto

data class StorePostListDto(
    val contents: List<StorePostDto> = listOf(),
    val cursor: CursorDto? = CursorDto(),
)

data class StorePostDto(
    val postId: String = "",
    val body: String = "",
    val sections: List<Section> = listOf(),
    val isOwner: Boolean = false,
    val store: Store = Store(),
    val stickers: List<Sticker> = listOf(),
    val createdAt: String = "",
    val updatedAt: String = "",
) {
    data class Section(
        val sectionType: StorePostSectionType = StorePostSectionType.UNKNOWN,
        val url: String = "",
        val ratio: Double = 1.0,
    )

    data class Store(
        val storeName: String = "",
        val categories: List<Category> = listOf(),
    ) {
        data class Category(
            val imageUrl: String = "",
        )
    }

    data class Sticker(
        val stickerId: String = "",
        val emoji: String = "",
        val count: Int = 0,
        val reactedByMe: Boolean = false,
    )
}

enum class StorePostSectionType {
    IMAGE,
    UNKNOWN;

    companion object {
        fun from(value: String?): StorePostSectionType =
            entries.firstOrNull { it.name == value } ?: UNKNOWN
    }
}

data class StorePostSectionRequestDto(
    val sectionType: StorePostSectionType = StorePostSectionType.IMAGE,
    val url: String,
    val ratio: Double,
)

data class StorePostCreateDto(
    val postId: String = "",
)
