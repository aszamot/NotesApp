package pl.atk.notes.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.atk.notes.framework.db.daos.NotesDao
import pl.atk.notes.framework.db.models.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao
}