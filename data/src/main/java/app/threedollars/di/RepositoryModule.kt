package app.threedollars.di

import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.repository.UserRepository
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
    abstract fun provideUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun provideStoreRepository(impl: StoreRepositoryImpl): StoreRepository
}