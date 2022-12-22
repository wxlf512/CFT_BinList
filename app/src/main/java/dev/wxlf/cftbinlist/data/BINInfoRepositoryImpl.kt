package dev.wxlf.cftbinlist.data

import dev.wxlf.cftbinlist.data.datasources.BINInfoLocalDataSource
import dev.wxlf.cftbinlist.data.datasources.BINInfoRemoteDataSource
import dev.wxlf.cftbinlist.data.entities.BINInfoEntity
import dev.wxlf.cftbinlist.domain.BINInfoRepository

class BINInfoRepositoryImpl(
    private val remote: BINInfoRemoteDataSource,
    private val local: BINInfoLocalDataSource
) : BINInfoRepository {
    override suspend fun fetchBINInfo(bin: String): BINInfoEntity =
        remote.loadBINInfo(bin)

    override suspend fun fetchRequestsHistory() =
        local.loadRequestsHistory()
}