package app.threedollars.common

data class ValueWrapper<T>(val value: T, val timestamp: Long = System.currentTimeMillis())