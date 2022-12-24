package dev.wxlf.cftbinlist.domain.repositories

import dev.wxlf.cftbinlist.data.entities.RequestEntity

interface RequestsRepository {

    suspend fun fetchRequestsHistory(): List<RequestEntity>
    suspend fun addRequest(requestEntity: RequestEntity)
    suspend fun deleteRequest(requestEntity: RequestEntity)

}