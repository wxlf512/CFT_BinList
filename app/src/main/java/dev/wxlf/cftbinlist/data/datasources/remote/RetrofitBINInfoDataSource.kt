package dev.wxlf.cftbinlist.data.datasources.remote

import dev.wxlf.cftbinlist.data.datasources.BINInfoRemoteDataSource
import dev.wxlf.cftbinlist.data.entities.BINInfoEntity
import dev.wxlf.cftbinlist.data.network.BINListApi

class RetrofitBINInfoDataSource(
    private val binListApi: BINListApi
) : BINInfoRemoteDataSource {
    override suspend fun loadBINInfo(bin: String): BINInfoEntity =
        binListApi.loadBINInfo(bin)

}