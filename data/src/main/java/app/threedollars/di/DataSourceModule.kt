package app.threedollars.di

import app.threedollars.source.LocalDataSource
import app.threedollars.source.LocalDataSourceImpl
import app.threedollars.source.RemoteDataSource
import app.threedollars.source.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun provideRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    @Singleton
    abstract fun provideLocalDataSource(impl: LocalDataSourceImpl): LocalDataSource
}