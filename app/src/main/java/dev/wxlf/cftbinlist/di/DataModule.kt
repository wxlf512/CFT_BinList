package dev.wxlf.cftbinlist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.wxlf.cftbinlist.data.repositories.BINInfoRepositoryImpl
import dev.wxlf.cftbinlist.data.datasources.RequestsLocalDataSource
import dev.wxlf.cftbinlist.data.datasources.BINInfoRemoteDataSource
import dev.wxlf.cftbinlist.data.datasources.local.RoomRequestsDataSource
import dev.wxlf.cftbinlist.data.datasources.local.room.RequestsDatabase
import dev.wxlf.cftbinlist.data.datasources.remote.RetrofitBINInfoDataSource
import dev.wxlf.cftbinlist.data.network.BINListApi
import dev.wxlf.cftbinlist.data.repositories.RequestsRepositoryImpl
import dev.wxlf.cftbinlist.domain.repositories.BINInfoRepository
import dev.wxlf.cftbinlist.domain.repositories.RequestsRepository

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideRetrofitDataSource(binListApi: BINListApi): BINInfoRemoteDataSource =
        RetrofitBINInfoDataSource(binListApi)

    @Provides
    fun provideRoomDataSource(requestsDatabase: RequestsDatabase): RequestsLocalDataSource =
        RoomRequestsDataSource(requestsDatabase.requestsHistoryDao())

    @Provides
    fun provideBINInfoRepository(
        remote: BINInfoRemoteDataSource
    ): BINInfoRepository = BINInfoRepositoryImpl(remote)

    @Provides
    fun provideRequestsRepository(
        local: RequestsLocalDataSource
    ): RequestsRepository = RequestsRepositoryImpl(local)
}