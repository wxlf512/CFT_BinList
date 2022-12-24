package dev.wxlf.cftbinlist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.wxlf.cftbinlist.data.entities.RequestEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val bin: String,
    val timestamp: String
) {
    companion object {
        const val TABLE_NAME = "requests_table"
    }
}