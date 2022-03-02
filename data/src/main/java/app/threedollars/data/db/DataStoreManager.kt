package app.threedollars.data.db

import android.content.Context
import androidx.datastore.preferences.core.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext val context: Context) {

    val moshi: Moshi = Moshi.Builder().build()

    suspend inline fun <reified T> toJson(data: List<T>): String {
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        return moshi.adapter<List<T>>(type).toJson(data) ?: ""
    }

    inline fun <reified T> toList(json: String): List<T> {
        val type: ParameterizedType = Types.newParameterizedType(List::class.java, T::class.java)
        return try {
            toList(type, json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun <T> toList(type: ParameterizedType, json: String) =
        moshi.adapter<List<T>>(type).fromJson(json) ?: emptyList()

    inline fun <reified T> getList(key: Preferences.Key<String>): Flow<List<T>> {
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        return context.dataStore.data.map {
            val json = it[key] ?: ""
            try {
                toList(type, json)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun getIntData(key: String): Flow<Int> {
        return context.dataStore.data
            .map { preferences ->
                preferences[intPreferencesKey(key)] ?: 0
            }
    }

    fun getStringData(key: String): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey(key)] ?: ""
            }
    }

    fun getBooleanData(key: String): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[booleanPreferencesKey(key)] ?: false
            }
    }

    suspend inline fun <reified T> saveList(
        key: String,
        data: List<T>,
    ) {
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = moshi.adapter<List<T>>(type).toJson(data)
        }
    }

    suspend fun saveIntData(key: String, value: Int?) {
        context.dataStore.edit { store ->
            store[intPreferencesKey(key)] = value ?: 0
        }
    }

    suspend fun saveStringData(key: String, value: String?) {
        context.dataStore.edit { store ->
            store[stringPreferencesKey(key)] = value ?: ""
        }
    }

    suspend fun saveBooleanData(key: String, value: Boolean) {
        context.dataStore.edit { store ->
            store[booleanPreferencesKey(key)] = value
        }
    }
}