package app.threedollars.di

import app.threedollars.domain.repository.AppConfigRepository
import app.threedollars.domain.repository.ReviewRepository
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.repository.UserRepository
import app.threedollars.repository.AppConfigRepositoryImpl
import app.threedollars.repository.ReviewRepositoryImpl
import app.threedollars.repository.StoreRepositoryImpl
import app.threedollars.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun provideUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    internal abstract fun provideStoreRepository(impl: StoreRepositoryImpl): StoreRepository

    @Binds
    @Singleton
    internal abstract fun provideReviewRepository(impl: ReviewRepositoryImpl): ReviewRepository

    @Binds
    @Singleton
    abstract fun provideAppConfigRepository(impl: AppConfigRepositoryImpl): AppConfigRepository
}