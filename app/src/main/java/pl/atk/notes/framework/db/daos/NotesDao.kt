package pl.atk.notes.framework.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.atk.notes.framework.db.models.NoteEntity
import java.util.UUID

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNote(noteId: UUID?): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteEntity: NoteEntity)

    @Update
    suspend fun update(noteEntity: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun delete(noteId: UUID)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getNoteFlow(noteId: UUID?): Flow<NoteEntity?>

    @Query("SELECT * FROM notes")
    fun getAllNotesFlow(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE CASE WHEN :query != '' THEN LOWER(title) LIKE '%' || LOWER(:query) || '%' OR LOWER(content) LIKE '%' || LOWER(:query) || '%' ELSE 0 END ")
    fun searchNotesFlow(query: String): Flow<List<NoteEntity>>

}