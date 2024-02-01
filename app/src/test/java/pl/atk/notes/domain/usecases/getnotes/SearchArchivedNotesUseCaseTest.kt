package pl.atk.notes.domain.usecases.getnotes

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.domain.utils.FilterNotesByType
import pl.atk.notes.domain.utils.SearchNotesQuery

class SearchArchivedNotesUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: SearchArchivedNotesUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = SearchArchivedNotesUseCase(notesRepository, dispatcherRule.testDispatcher)
    }

    @Test
    fun invoke_shouldUseRepositorySearchNotesFlowWithQuery() = runTest {
        val query = "test"
        useCase.invoke(query)

        val searchNotesQueryCaptor = argumentCaptor<SearchNotesQuery>()
        verify(notesRepository).searchNotesFlow(
            searchNotesQuery = searchNotesQueryCaptor.capture(),
            filterNotesByType = eq(FilterNotesByType.Archived)
        )

        assertEquals(query, searchNotesQueryCaptor.firstValue.query)
    }

    @Test
    fun invoke_shouldReturnSearchedNotesFromRepository() = runTest {
        val query = "test"
        val searchedNotes = listOf(
            TestData.TEST_NOTE_1.copy(isArchived = true),
            TestData.TEST_NOTE_2.copy(isArchived = true)
        )
        Mockito.doReturn(flowOf(searchedNotes))
            .whenever(notesRepository)
            .searchNotesFlow(SearchNotesQuery(query), FilterNotesByType.Archived)

        useCase.invoke(query).test {
            val list = awaitItem()
            assertEquals(searchedNotes, list)
            awaitComplete()
        }
    }
}