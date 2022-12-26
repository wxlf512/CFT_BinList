package dev.wxlf.cftbinlist.data.network.repositories

import dev.wxlf.cftbinlist.data.datasources.BINInfoRemoteDataSource
import dev.wxlf.cftbinlist.data.entities.BINInfoEntity
import dev.wxlf.cftbinlist.domain.repositories.BINInfoRepository

class BINInfoRepositoryImpl(
    private val remote: BINInfoRemoteDataSource
) : BINInfoRepository {
    override suspend fun fetchBINInfo(bin: String): BINInfoEntity =
        remote.loadBINInfo(bin)
}