package dev.wxlf.cftbinlist.domain.repositories

import dev.wxlf.cftbinlist.data.entities.BINInfoEntity

interface BINInfoRepository {

    suspend fun fetchBINInfo(bin: String): BINInfoEntity

}