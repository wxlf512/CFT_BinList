package dev.wxlf.cftbinlist.data.datasources.local

import dev.wxlf.cftbinlist.data.datasources.RequestsLocalDataSource
import dev.wxlf.cftbinlist.data.datasources.local.room.RequestsHistoryDao
import dev.wxlf.cftbinlist.data.entities.RequestEntity

class RoomRequestsDataSource(private val requestsHistoryDao: RequestsHistoryDao) : RequestsLocalDataSource {

    override suspend fun loadRequestsHistory(): List<RequestEntity> =
        requestsHistoryDao.loadRequestsHistory()

    override suspend fun addRequest(requestEntity: RequestEntity) =
        requestsHistoryDao.addRequest(requestEntity)

    override suspend fun deleteRequest(requestEntity: RequestEntity) =
        requestsHistoryDao.deleteRequest(requestEntity)
}