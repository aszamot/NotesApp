package pl.atk.notes.domain.usecases.getnotes

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doReturn
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.atk.notes.TestData
import pl.atk.notes.TestDispatcherRule
import pl.atk.notes.domain.repository.NotesRepository
import pl.atk.notes.domain.utils.SearchNotesQuery

class SearchNotesUseCaseTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var useCase: SearchNotesUseCase
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        notesRepository = mock()
        useCase = SearchNotesUseCase(notesRepository, dispatcherRule.testDispatcher)
    }

    @Test
    fun invoke_shouldUseRepositorySearchNotesFlowWithQuery() = runTest {
        val query = "test"
        useCase.invoke(query)

        val searchNotesQueryCaptor = argumentCaptor<SearchNotesQuery>()
        verify(notesRepository).searchNotesFlow(
            searchNotesQuery = searchNotesQueryCaptor.capture()
        )

        assertEquals(query, searchNotesQueryCaptor.firstValue.query)
    }

    @Test
    fun invoke_shouldReturnSearchedNotesFromRepository() = runTest {
        val query = "test"
        val searchedNotes = listOf(TestData.TEST_NOTE_1, TestData.TEST_NOTE_2)
        doReturn(flowOf(searchedNotes)).whenever(notesRepository)
            .searchNotesFlow(SearchNotesQuery(query))

        useCase.invoke(query).test {
            val list = awaitItem()
            assertEquals(searchedNotes, list)
            awaitComplete()
        }
    }
}