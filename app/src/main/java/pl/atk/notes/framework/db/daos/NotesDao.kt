package pl.atk.notes.framework.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.atk.notes.framework.db.models.NoteEntity

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteEntity: NoteEntity)

    @Delete
    suspend fun delete(noteEntity: NoteEntity)

    @Query("SELECT * FROM notes")
    fun getNotesFlow(): Flow<List<NoteEntity>>
}