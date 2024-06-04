package pl.atk.notes.framework.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: UUID,
    val title: String?,
    val content: String?,
    val timestamp: Long
)