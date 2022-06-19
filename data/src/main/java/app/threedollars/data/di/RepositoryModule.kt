package app.threedollars.data.di

import app.threedollars.data.datastore.SignDataSource
import app.threedollars.data.datastore.SignDataSourceImpl
import app.threedollars.data.datastore.SignRepositoryImpl
import app.threedollars.domain.repository.SignRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideSignDataSource(signDataSource: SignDataSourceImpl): SignDataSource

    @Binds
    @Singleton
    abstract fun provideSignRepository(repositoryImpl: SignRepositoryImpl): SignRepository
}