package app.threedollars.data.request

import app.threedollars.domain.dto.StorePostSectionRequestDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StorePostRequest(
    @SerialName("body")
    val body: String,
    @SerialName("sections")
    val sections: List<Section>,
) {
    @Serializable
    internal data class Section(
        @SerialName("sectionType")
        val sectionType: String,
        @SerialName("url")
        val url: String,
        @SerialName("ratio")
        val ratio: Double,
    )

    companion object {
        fun from(body: String, sections: List<StorePostSectionRequestDto>) = StorePostRequest(
            body = body,
            sections = sections.map {
                Section(sectionType = it.sectionType.name, url = it.url, ratio = it.ratio)
            },
        )
    }
}
