package dev.wxlf.cftbinlist.data.datasources.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.wxlf.cftbinlist.data.entities.RequestEntity

@Database(
    entities = [RequestEntity::class],
    version = 1,
    exportSchema = true
)
abstract class RequestsDatabase : RoomDatabase() {

    abstract fun requestsHistoryDao(): RequestsHistoryDao
}