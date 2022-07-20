package app.threedollars.data.di

import app.threedollars.data.datastore.SignRepositoryImpl
import app.threedollars.domain.repository.SignRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideSignRepository(repositoryImpl: SignRepositoryImpl): SignRepository
}