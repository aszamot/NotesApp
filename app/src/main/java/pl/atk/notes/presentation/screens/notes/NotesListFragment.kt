package pl.atk.notes.presentation.screens.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.search.SearchView.TransitionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.atk.notes.R
import pl.atk.notes.databinding.FragmentNotesListBinding
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.presentation.model.NoteItemUi
import pl.atk.notes.presentation.screens.base.BaseFragment
import pl.atk.notes.presentation.screens.confirmation.ConfirmationDialog
import pl.atk.notes.presentation.utils.adapters.NotesAdapter
import pl.atk.notes.utils.extensions.empty
import pl.atk.notes.utils.extensions.setupToolbarOffset
import java.util.UUID

@AndroidEntryPoint
class NotesListFragment : BaseFragment<FragmentNotesListBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNotesListBinding =
        FragmentNotesListBinding::inflate

    private val viewModel: NotesListViewModel by viewModels()

    private var notesAdapter: NotesAdapter? = null
    private var searchNotesAdapter: NotesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeUiState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        notesAdapter = null
        searchNotesAdapter = null
    }

    private fun setupViews() {
        setupFabAddNote()
        setupSearchView()
        setupToolbar()
        setupSwipeRefreshLayout()
        setupNotesRecyclerView()
        setupSearchNotesRecyclerView()
    }

    private fun setupFabAddNote() {
        binding.fabAddNote.setOnClickListener {
            goToNotesDetailsFragment(null)
        }
    }

    private fun goToNotesDetailsFragment(noteId: UUID?) {
        val action = NotesListFragmentDirections.actionNotesListFragmentToNoteDetailsFragment(
            noteId = noteId
        )
        findNavController().navigate(action)
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.editText.doOnTextChanged { text, _, _, _ ->
                viewModel.setQuery(text.toString())
            }
            searchView.addTransitionListener { _, _, newTransitionState ->
                viewModel.setInSearchMode(newTransitionState == TransitionState.SHOWN || newTransitionState == TransitionState.SHOWING)
            }
        }
    }

    private fun setupToolbar() {
        with(binding) {
            chooseToolbar.setNavigationOnClickListener { viewModel.removeAllSelectedNote() }
            chooseToolbar.inflateMenu(R.menu.menu_delete)
            chooseToolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_delete -> showDeleteConfirmationDialog()
                }
                false
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialog =
            ConfirmationDialog(
                confirmationQuestionId = R.string.notes_delete_confirmation_question,
                onConfirm = {
                    viewModel.deleteSelectedNotes()
                })
        dialog.show(childFragmentManager, ConfirmationDialog.TAG)
    }

    private fun setupSwipeRefreshLayout() {
        with(binding.swipeToRefresh) {
            setupToolbarOffset()
            setOnRefreshListener { viewModel.observeAllNotesFlow() }
        }
    }

    private fun setupNotesRecyclerView() {
        binding.recyclerNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            notesAdapter = NotesAdapter(
                onNoteClickListener = ::onNoteClick,
                onNoteLongClickListener = ::onNoteLongClick
            )
            adapter = notesAdapter
        }
    }

    private fun onNoteClick(note: NoteItemUi) {
        if (viewModel.isInNoteSelectedMode()) {
            viewModel.selectNoteToggle(note)
        } else {
            goToNotesDetailsFragment(note.id)
        }
    }

    private fun onNoteLongClick(note: NoteItemUi) {
        viewModel.selectNoteToggle(note)
    }

    private fun setupSearchNotesRecyclerView() {
        binding.recyclerSearchNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            searchNotesAdapter = NotesAdapter(
                onNoteClickListener = ::onNoteClick
            )
            adapter = searchNotesAdapter
        }
    }

    private fun observeUiState() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { uiState ->
                renderUiState(uiState)
            }
            .launchIn(lifecycleScope)
    }

    private fun renderUiState(uiState: NotesListUiState) {
        with(binding) {
            swipeToRefresh.isRefreshing = uiState.isLoading

            notesAdapter?.submitList(uiState.notes)
            emptyViewNotes.isVisible = uiState.notesEmpty

            searchNotesAdapter?.submitList(uiState.searchedNotes)
            emptyViewSearchNotes.isVisible = uiState.searchedNotesEmpty && !uiState.searchQueryEmpty

            setToolbarSelectedNotesCount(uiState.selectedNotesCount)
            setNoteSelectedMode(uiState.isInNoteSelectedMode)

            fabAddNote.isVisible = !uiState.isInSearch

            if (uiState.message != null) {
                showSnackbar(uiState.message)
                viewModel.consumeMessage()
            }

            if (uiState.error != null) {
                when (uiState.error) {
                    is NoteNotFoundException -> showSnackbar(R.string.error_note_not_found)
                    else -> showSnackbar(R.string.error)
                }
                viewModel.consumeError()
            }
        }
    }

    private fun setToolbarSelectedNotesCount(count: Int) {
        binding.chooseToolbar.title = if (count > 0)
            count.toString()
        else
            String.empty
    }

    private fun setNoteSelectedMode(isNoteSelected: Boolean) {
        with(binding) {
            if (isNoteSelected) {
                searchBar.expand(chooseToolbar, appBarLayout)
            } else {
                searchBar.collapse(chooseToolbar, appBarLayout)
            }
        }
    }
}