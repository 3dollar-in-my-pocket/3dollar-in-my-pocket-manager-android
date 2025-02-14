package app.threedollars.domain.dto

data class CommentPresetDto(
    val contents: List<CommentPreset> = listOf(),
) {
    data class CommentPreset(
        val presetId: String,
        val body: String,
        val createdAt: String,
        val updateAt: String
    )
}
