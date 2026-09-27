package app.threedollars.common

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

private val errorJson = Json { ignoreUnknownKeys = true; isLenient = true }

/**
 * 서버 에러 바디(`{"ok":false,"resultCode":…,"message":"…"}`)에서 사용자에게 보여줄 `message` 만 꺼낸다.
 * JSON 이 아니면 원문을, JSON 인데 message 가 없으면 null 을 돌려준다(호출부 기본 문구 사용).
 */
fun String?.toDisplayErrorMessage(): String? {
    val raw = this?.trim().orEmpty()
    if (raw.isEmpty()) return null
    val json = runCatching { errorJson.parseToJsonElement(raw) as? JsonObject }.getOrNull() ?: return raw
    return runCatching { json["message"]?.jsonPrimitive?.contentOrNull }.getOrNull()?.takeIf { it.isNotBlank() }
}
