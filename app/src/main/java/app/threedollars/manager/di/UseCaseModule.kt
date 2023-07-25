package app.threedollars.manager.di

import app.threedollars.domain.usecase.*
import app.threedollars.manager.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
abstract class UseCaseModule {

    @ViewModelScoped
    @Binds
    abstract fun bindAuthUseCase(impl: AuthUseCaseImpl): AuthUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindBossAccountUseCase(impl: BossAccountUseCaseImpl): BossAccountUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindBossDeviceUseCase(impl: BossDeviceUseCaseImpl): BossDeviceUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindBossStoreOpenUseCase(impl: BossStoreOpenUseCaseImpl): BossStoreOpenUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindBossStoreRetrieveUseCase(impl: BossStoreRetrieveUseCaseImpl): BossStoreRetrieveUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindBossStoreUseCase(impl: BossStoreUseCaseImpl): BossStoreUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindEnumMapperUseCase(impl: EnumMapperUseCaseImpl): EnumMapperUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindFaqUseCase(impl: FaqUseCaseImpl): FaqUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindFeedbackUseCase(impl: FeedbackUseCaseImpl): FeedbackUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindImageUploadUseCase(impl: ImageUploadUseCaseImpl): ImageUploadUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindPlatformStoreCategoryUseCase(impl: PlatformStoreCategoryUseCaseImpl): PlatformStoreCategoryUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindAppConfigUseCase(impl: AppConfigUseCaseImpl): AppConfigUseCase
}