package dev.wxlf.cftbinlist.data.datasources

import dev.wxlf.cftbinlist.data.entities.RequestEntity

interface RequestsLocalDataSource {

    suspend fun loadRequestsHistory() : List<RequestEntity>
    suspend fun addRequest(requestEntity: RequestEntity)
    suspend fun deleteRequest(requestEntity: RequestEntity)

}