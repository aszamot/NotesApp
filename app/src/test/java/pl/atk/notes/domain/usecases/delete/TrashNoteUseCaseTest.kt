package pl.atk.notes.domain.usecases.delete

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.repository.NotesRepository

class TrashNoteUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: TrashNoteUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = TrashNoteUseCase(notesRepository)
    }

    @Test
    fun invoke_shouldCallNotesRepositoryTrashNote() = runTest {
        val note = TestData.TEST_NOTE_1

        useCase.invoke(note)

        verify(notesRepository).trashNote(note)
    }
}