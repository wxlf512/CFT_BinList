package dev.wxlf.cftbinlist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.wxlf.cftbinlist.domain.BINInfoRepository
import dev.wxlf.cftbinlist.domain.usecases.FetchBINInfoUseCase

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideFetchBINInfoUseCase(repository: BINInfoRepository): FetchBINInfoUseCase =
        FetchBINInfoUseCase(repository)
}