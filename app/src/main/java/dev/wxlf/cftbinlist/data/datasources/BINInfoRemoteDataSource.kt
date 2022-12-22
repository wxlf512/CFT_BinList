package dev.wxlf.cftbinlist.data.datasources

import dev.wxlf.cftbinlist.data.entities.BINInfoEntity

interface BINInfoRemoteDataSource {

    suspend fun loadBINInfo(bin: String) : BINInfoEntity
}