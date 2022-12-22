package dev.wxlf.cftbinlist.data.datasources

interface BINInfoLocalDataSource {

    suspend fun loadRequestsHistory()
}