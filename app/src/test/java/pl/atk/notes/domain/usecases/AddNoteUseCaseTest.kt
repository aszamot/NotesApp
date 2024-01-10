package pl.atk.notes.domain.usecases

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import pl.atk.notes.TestData
import pl.atk.notes.domain.repository.NotesRepository

class AddNoteUseCaseTest {

    private lateinit var useCase: AddNoteUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = AddNoteUseCase(notesRepository)
    }

    @Test
    fun invoke_shouldCallNotesRepositoryAddNote() = runTest {
        val note = TestData.TEST_NOTE_1

        useCase.invoke(note)

        verify(notesRepository).addNote(note)
    }
}