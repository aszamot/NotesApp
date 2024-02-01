package pl.atk.notes.domain.usecases.getnotes

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.domain.utils.FilterNotesByType

class GetTrashNotesUseCaseTest {
    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: GetTrashNotesUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = GetTrashNotesUseCase(notesRepository, dispatcherRule.testDispatcher)
    }

    @Test
    fun invoke_shouldUseRepositoryGetTrashFlowWithNullFilter() = runTest {
        useCase.invoke()

        verify(notesRepository).getAllNotesFlow(FilterNotesByType.InTrash)
    }

    @Test
    fun invoke_shouldReturnNotesFromNotesRepository() = runTest {
        val notes = listOf(
            TestData.TEST_NOTE_1.copy(isInTrash = true),
            TestData.TEST_NOTE_2.copy(isInTrash = true)
        )
        whenever(notesRepository.getAllNotesFlow(FilterNotesByType.InTrash)).thenReturn(
            flowOf(
                notes
            )
        )

        useCase.invoke().test {
            val list = awaitItem()
            assertEquals(notes, list)
            awaitComplete()
        }
    }
}