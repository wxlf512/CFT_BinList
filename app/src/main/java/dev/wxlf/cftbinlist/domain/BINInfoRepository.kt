package dev.wxlf.cftbinlist.domain

import dev.wxlf.cftbinlist.data.entities.BINInfoEntity

interface BINInfoRepository {

    suspend fun fetchBINInfo(bin: String): BINInfoEntity

    suspend fun fetchRequestsHistory()
}