package app.threedollars.data.di

import app.threedollars.data.BuildConfig
import app.threedollars.data.user.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface NetworkModule {

    @Provides
    @Singleton
    fun provideLoggerInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.BUILD_TYPE == "debug") HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(httpLoggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor {
            val request = it.request().newBuilder()
                .addHeader("X-ANDROID-SERVICE-VERSION", BuildConfig.BUILD_TYPE + "_0.0.0")
                .build()
            it.proceed(request)
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)
}