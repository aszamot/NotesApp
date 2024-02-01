package pl.atk.notes.presentation.screens.archive

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
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.atk.notes.R
import pl.atk.notes.databinding.FragmentArchiveBinding
import pl.atk.notes.domain.exceptions.NoteIsInTrashException
import pl.atk.notes.domain.exceptions.NoteNotFoundException
import pl.atk.notes.presentation.model.NoteItemUi
import pl.atk.notes.presentation.screens.base.BaseFragment
import pl.atk.notes.presentation.screens.menu.CurrentMenuItem
import pl.atk.notes.presentation.screens.notes.NotesListUiState
import pl.atk.notes.presentation.utils.adapters.NotesAdapter
import pl.atk.notes.utils.extensions.empty

@AndroidEntryPoint
class ArchiveFragment : BaseFragment<FragmentArchiveBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentArchiveBinding =
        FragmentArchiveBinding::inflate

    private val viewModel: ArchiveViewModel by viewModels()

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
        setupSearchView()
        setupToolbar()
        setupSwipeRefreshLayout()
        setupNotesRecyclerView()
        setupSearchNotesRecyclerView()
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.editText.doOnTextChanged { text, _, _, _ ->
                viewModel.setQuery(text.toString())
            }
            searchView.addTransitionListener { _, _, newTransitionState ->
                viewModel.setInSearchMode(newTransitionState == SearchView.TransitionState.SHOWN || newTransitionState == SearchView.TransitionState.SHOWING)
            }
        }
    }

    private fun setupToolbar() {
        with(binding) {
            archiveToolbar.setNavigationOnClickListener { showMenuDialog() }
            archiveToolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_search -> {
                        binding.searchView.show()
                        true
                    }

                    else -> false
                }
            }
            archiveActionToolbar.setNavigationOnClickListener { viewModel.removeAllSelectedNote() }
            archiveActionToolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_unarchive -> {
                        viewModel.unArchiveSelectedNotes()
                        true
                    }

                    R.id.menu_trash -> {
                        viewModel.trashSelectedNotes()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun showMenuDialog() {
        val action = ArchiveFragmentDirections.actionArchiveFragmentToMenuDialog(
            currentMenuItem = CurrentMenuItem.ARCHIVE
        )
        findNavController().navigate(action)
    }

    private fun setupSwipeRefreshLayout() {
        with(binding.swipeToRefresh) {
            setOnRefreshListener { viewModel.observeArchivedNotesFlow() }
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
            showSnackbar("GO TO EDIT NOTE")
        }
    }

    private fun onNoteLongClick(note: NoteItemUi) {
        viewModel.selectNoteToggle(note)
    }

    private fun setupSearchNotesRecyclerView() {
        binding.recyclerSearchNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            searchNotesAdapter = NotesAdapter()
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
            emptyArchive.isVisible = uiState.notesEmpty

            searchNotesAdapter?.submitList(uiState.searchedNotes)
            emptyViewSearchNotes.isVisible = uiState.searchedNotesEmpty && !uiState.searchQueryEmpty

            setToolbarSelectedNotesCount(uiState.selectedNotesCount)
            setNoteSelectedMode(uiState.isInNoteSelectedMode)

            if (uiState.message != null) {
                showSnackbar(uiState.message)
                viewModel.consumeMessage()
            }

            if (uiState.error != null) {
                when (uiState.error) {
                    is NoteIsInTrashException -> showSnackbar(R.string.error_archive_note_in_trash)
                    is NoteNotFoundException -> showSnackbar(R.string.error_note_not_found)
                    else -> showSnackbar(R.string.error)
                }
                viewModel.consumeError()
            }
        }
    }

    private fun setToolbarSelectedNotesCount(count: Int) {
        binding.archiveActionToolbar.title = if (count > 0)
            count.toString()
        else
            String.empty
    }

    private fun setNoteSelectedMode(isNoteSelected: Boolean) {
        with(binding) {
            archiveToolbar.isVisible = !isNoteSelected
            archiveActionToolbar.isVisible = isNoteSelected
        }
    }
}