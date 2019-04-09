package com.example.shreya.makememories.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "memories_table")
data class MemoryEntity(

        @ColumnInfo(name = "image_reference")
        val imageReference: String? = "",

        @ColumnInfo(name = "image_caption")
        val imageCaption: String,

        @ColumnInfo(name = "image_description")
        val imageDescription: String)
{
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "memory_id")
        val id: Int = 0
}
