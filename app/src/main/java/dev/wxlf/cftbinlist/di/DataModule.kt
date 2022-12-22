package dev.wxlf.cftbinlist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.wxlf.cftbinlist.data.BINInfoRepositoryImpl
import dev.wxlf.cftbinlist.data.datasources.BINInfoLocalDataSource
import dev.wxlf.cftbinlist.data.datasources.BINInfoRemoteDataSource
import dev.wxlf.cftbinlist.data.datasources.local.RoomBINInfoDataSource
import dev.wxlf.cftbinlist.data.datasources.remote.RetrofitBINInfoDataSource
import dev.wxlf.cftbinlist.data.network.BINListApi
import dev.wxlf.cftbinlist.domain.BINInfoRepository

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideRetrofitDataSource(binListApi: BINListApi): BINInfoRemoteDataSource =
        RetrofitBINInfoDataSource(binListApi)

    @Provides
    fun provideRoomDataSource(): BINInfoLocalDataSource =
        RoomBINInfoDataSource()

    @Provides
    fun provideBINInfoRepository(
        remote: BINInfoRemoteDataSource,
        local: BINInfoLocalDataSource
    ): BINInfoRepository = BINInfoRepositoryImpl(remote, local)
}