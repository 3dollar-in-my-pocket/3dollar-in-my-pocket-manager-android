package app.threedollars.di

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import app.threedollars.data.BuildConfig
import app.threedollars.db.DataStoreManager
import app.threedollars.network.MaintenanceInterceptor
import app.threedollars.network.NetworkService
import app.threedollars.source.LocalDataSourceImpl.Companion.ACCESS_TOKEN
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggerInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.BUILD_TYPE == "debug") HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideMaintenanceInterceptor(): MaintenanceInterceptor = MaintenanceInterceptor()

    @Provides
    @Singleton
    fun provideHeaderInterceptor(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        maintenanceInterceptor: MaintenanceInterceptor,
        dataStoreManager: DataStoreManager,
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val applicationId = context.packageName
        val versionName = context.appVersionName()
        return OkHttpClient.Builder()
            .addInterceptor {
                val token = runBlocking { dataStoreManager.getStringData(ACCESS_TOKEN).firstOrNull() ?: "" }

                val request = it.request().newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", versionName + " (${applicationId}); " + Build.VERSION.SDK_INT)
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("X-ANDROID-SERVICE-VERSION", BuildConfig.BUILD_TYPE + "_" + versionName)
                    .build()
                it.proceed(request)
            }
            .addInterceptor(maintenanceInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    private fun Context.appVersionName(): String = try {
        packageManager.getPackageInfo(packageName, 0).versionName.orEmpty()
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient,
        jsonConverter: Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(jsonConverter)
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideConverterFactory(
        json: Json,
    ): Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
    internal fun provideNetworkService(retrofit: Retrofit): NetworkService = retrofit.create(NetworkService::class.java)
}