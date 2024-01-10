package pl.atk.notes.domain.usecases.archive

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.exceptions.NoteIsInTrashException
import pl.atk.notes.domain.repository.NotesRepository

class ArchiveNoteUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: ArchiveNoteUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = ArchiveNoteUseCase(notesRepository)
    }

    @Test
    fun invoke_noteNotDeleted_shouldCallNotesRepositoryArchiveNote() = runTest {
        val note = TestData.TEST_NOTE_1

        useCase.invoke(note)

        verify(notesRepository).archiveNote(note)
    }

    @Test(expected = NoteIsInTrashException::class)
    fun invoke_noteDeleted_shouldThrowNoteIsDeletedException() = runTest {
        val note = TestData.TEST_NOTE_1_DELETED

        useCase.invoke(note)
    }
}