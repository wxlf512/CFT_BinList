package dev.wxlf.cftbinlist.data.datasources.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.wxlf.cftbinlist.data.entities.RequestEntity

@Dao
interface RequestsHistoryDao {

    @Query("SELECT * FROM ${RequestEntity.TABLE_NAME}")
    suspend fun loadRequestsHistory(): List<RequestEntity>

    @Insert(entity = RequestEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRequest(requestEntity: RequestEntity)

    @Delete(entity = RequestEntity::class)
    suspend fun deleteRequest(requestEntity: RequestEntity)
}