package dev.wxlf.cftbinlist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.wxlf.cftbinlist.domain.repositories.BINInfoRepository
import dev.wxlf.cftbinlist.domain.repositories.RequestsRepository
import dev.wxlf.cftbinlist.domain.usecases.AddRequestToHistoryUseCase
import dev.wxlf.cftbinlist.domain.usecases.DeleteRequestFromHistoryUseCase
import dev.wxlf.cftbinlist.domain.usecases.FetchBINInfoUseCase
import dev.wxlf.cftbinlist.domain.usecases.FetchRequestsHistoryUseCase

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideFetchBINInfoUseCase(repository: BINInfoRepository): FetchBINInfoUseCase =
        FetchBINInfoUseCase(repository)

    @Provides
    fun provideFetchRequestsHistoryUseCase(repository: RequestsRepository): FetchRequestsHistoryUseCase =
        FetchRequestsHistoryUseCase(repository)

    @Provides
    fun provideAddRequestToHistoryUseCase(repository: RequestsRepository): AddRequestToHistoryUseCase =
        AddRequestToHistoryUseCase(repository)

    @Provides
    fun provideDeleteRequestFromHistoryUseCase(repository: RequestsRepository): DeleteRequestFromHistoryUseCase =
        DeleteRequestFromHistoryUseCase(repository)
}