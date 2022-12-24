package dev.wxlf.cftbinlist.data.repositories

import dev.wxlf.cftbinlist.data.datasources.RequestsLocalDataSource
import dev.wxlf.cftbinlist.data.entities.RequestEntity
import dev.wxlf.cftbinlist.domain.repositories.RequestsRepository

class RequestsRepositoryImpl(
    private val local: RequestsLocalDataSource
) : RequestsRepository {
    override suspend fun fetchRequestsHistory(): List<RequestEntity> =
        local.loadRequestsHistory()

    override suspend fun addRequest(requestEntity: RequestEntity) =
        local.addRequest(requestEntity)

    override suspend fun deleteRequest(requestEntity: RequestEntity) =
        local.deleteRequest(requestEntity)

}