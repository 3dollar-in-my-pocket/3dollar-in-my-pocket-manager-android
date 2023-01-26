package app.threedollars.data.di

import app.threedollars.source.RemoteDataSource
import app.threedollars.source.RemoteDataSourceImpl
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {
    @Provides
    @Singleton
    abstract fun provideRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource
}