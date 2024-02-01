package pl.atk.notes.framework.db.daos

import androidx.room.Dao
import androidx.room.Delete
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
    suspend fun getNote(noteId: UUID): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteEntity: NoteEntity)

    @Update
    suspend fun update(noteEntity: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun delete(noteId: UUID)

    @Query("SELECT * FROM notes WHERE is_archived = 0 AND is_in_trash = 0")
    fun getAllNotesFlow(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE is_archived = 0 AND is_in_trash = 0 AND CASE WHEN :query != '' THEN LOWER(title) LIKE '%' || LOWER(:query) || '%' OR LOWER(content) LIKE '%' || LOWER(:query) || '%' ELSE 0 END ")
    fun searchNotesFlow(query: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE is_archived = 1 AND is_in_trash = 0")
    fun getArchivedNotesFlow(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE is_archived = 1 AND is_in_trash = 0 AND CASE WHEN :query != '' THEN LOWER(title) LIKE '%' || LOWER(:query) || '%' OR LOWER(content) LIKE '%' || LOWER(:query) || '%' ELSE 0 END ")
    fun searchArchivedNotesFlow(query: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE is_in_trash = 1")
    fun getInTrashNotesFlow(): Flow<List<NoteEntity>>
}