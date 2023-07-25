package app.threedollars.common.ext

fun String?.toStringDefault(default: String = ""): String {
    return this ?: default
}

fun Int?.toIntDefault(default: Int = 0): Int {
    return this ?: default
}

fun Long?.toLongDefault(default: Long = 0L): Long {
    return this ?: default
}

fun Float?.toFloatDefault(default: Float = 0f): Float {
    return this ?: default
}

fun Double?.toDoubleDefault(default: Double = 0.0): Double {
    return this ?: default
}

fun Char?.toCharDefault(default: Char = '\u0000'): Char {
    return this ?: default
}

fun Boolean?.toBooleanDefault(default: Boolean = false): Boolean {
    return this ?: default
}
