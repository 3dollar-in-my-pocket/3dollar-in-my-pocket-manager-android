package app.threedollars.data.di

import app.threedollars.data.BuildConfig
import app.threedollars.data.user.KakaoLoginApi
import app.threedollars.data.user.StoreService
import app.threedollars.data.user.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val kakaoAuthURL = "https://kauth.kakao.com/"

    @Provides
    @Singleton
    fun provideLoggerInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.BUILD_TYPE == "debug") HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
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
    @Named("user")
    fun provideManagerRetrofitBuilder(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("kakao")
    fun provideKakaoRetrofitBuilder(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(kakaoAuthURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(@Named("user") retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideStoreService(@Named("user") retrofit: Retrofit): StoreService =
        retrofit.create(StoreService::class.java)

    @Provides
    @Singleton
    fun provideKakaoService(@Named("kakao") retrofit: Retrofit): KakaoLoginApi =
        retrofit.create(KakaoLoginApi::class.java)
}