package pl.atk.notes.presentation.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.atk.notes.R
import pl.atk.notes.domain.usecases.archive.ArchiveNoteUseCase
import pl.atk.notes.domain.usecases.getnotes.GetAllNotesUseCase
import pl.atk.notes.domain.usecases.delete.TrashNoteUseCase
import pl.atk.notes.domain.usecases.getnotes.SearchNotesUseCase
import pl.atk.notes.presentation.model.NoteItemUi
import pl.atk.notes.utils.extensions.empty
import pl.atk.notes.utils.extensions.toNoteItemUi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val archiveNoteUseCase: ArchiveNoteUseCase,
    private val trashNoteUseCase: TrashNoteUseCase
) : ViewModel() {

    private val _query = MutableStateFlow(String.empty)
    private val _notesSelected = MutableStateFlow<MutableSet<NoteItemUi>>(mutableSetOf())

    private val _uiState = MutableStateFlow(NotesListUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        NotesListUiState()
    )

    init {
        observeAllNotesFlow()
        observeQueryFlowAndSearchForNotes()
        observeSelectedNotes()
    }

    fun observeAllNotesFlow() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        getAllNotesUseCase.invoke()
            .map { notes ->
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        notes = notes.map { it.toNoteItemUi() },
                        notesEmpty = notes.isEmpty()
                    )
            }.catch { e ->
                setError(e)
            }.launchIn(viewModelScope)
    }

    private fun observeQueryFlowAndSearchForNotes() {
        _query
            .flatMapLatest { query ->
                _uiState.value = _uiState.value.copy(searchQueryEmpty = query.isEmpty())
                searchNotesUseCase.invoke(query)
            }.map { notes ->
                _uiState.value =
                    _uiState.value.copy(
                        searchedNotes = notes.map { it.toNoteItemUi() },
                        searchedNotesEmpty = notes.isEmpty()
                    )
            }
            .catch { e ->
                setError(e)
            }.launchIn(viewModelScope)
    }

    private fun observeSelectedNotes() {
        _notesSelected.onEach { selectedNotes ->

            _uiState.value = _uiState.value.copy(
                selectedNotesCount = selectedNotes.size,
                isInNoteSelectedMode = selectedNotes.isNotEmpty(),
                notes = _uiState.value.notes.map { it.copy(isSelected = selectedNotes.contains(it)) }
            )
        }.launchIn(viewModelScope)
    }

    private fun setError(e: Throwable?) {
        _uiState.value = _uiState.value.copy(error = e)
    }

    fun setQuery(query: String) {
        _query.value = query
    }

    fun selectNoteToggle(note: NoteItemUi) {
        val newSet = _notesSelected.value.toMutableSet()
        if (newSet.contains(note)) {
            newSet.remove(note)
        } else {
            newSet.add(note)
        }
        _notesSelected.value = newSet
    }

    fun removeAllSelectedNote() {
        _notesSelected.value = mutableSetOf()
    }

    fun setInSearchMode(inMode: Boolean) {
        _uiState.value = _uiState.value.copy(isInSearch = inMode)
    }

    fun isInNoteSelectedMode() = _uiState.value.isInNoteSelectedMode

    fun archiveNote(noteItemUi: NoteItemUi) {
        viewModelScope.launch {
            try {
                archiveNoteUseCase.invoke(noteItemUi.id)
                _uiState.value = _uiState.value.copy(message = R.string.note_archived)
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    fun archiveSelectedNotes() {
        viewModelScope.launch {
            try {
                _notesSelected.value.forEach { note ->
                    archiveNoteUseCase.invoke(noteId = note.id)
                }
                _uiState.value = _uiState.value.copy(message = R.string.notes_archived)
                removeAllSelectedNote()
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    fun trashSelectedNotes() {
        viewModelScope.launch {
            try {
                _notesSelected.value.forEach { note ->
                    trashNoteUseCase.invoke(noteId = note.id)
                }
                _uiState.value = _uiState.value.copy(message = R.string.notes_trashed)
                removeAllSelectedNote()
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    fun consumeMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun consumeError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}