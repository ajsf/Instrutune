package tech.ajsf.instrutune.common.data.db

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "instrument", indices = [Index(value = ["category"])], primaryKeys = ["tuningName", "category"])
data class InstrumentEntity(
    val tuningName: String,
    val category: String,
    val numberedNotes: List<String>
)
