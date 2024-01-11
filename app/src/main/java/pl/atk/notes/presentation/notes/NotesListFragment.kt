package pl.atk.notes.presentation.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import pl.atk.notes.databinding.FragmentNotesListBinding
import pl.atk.notes.presentation.base.BaseFragment

@AndroidEntryPoint
class NotesListFragment : BaseFragment<FragmentNotesListBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNotesListBinding =
        FragmentNotesListBinding::inflate


}