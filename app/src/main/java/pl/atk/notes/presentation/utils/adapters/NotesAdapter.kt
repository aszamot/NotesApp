package pl.atk.notes.presentation.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.atk.notes.databinding.ItemNoteBinding
import pl.atk.notes.presentation.model.NoteItemUi
import pl.atk.notes.utils.extensions.dpToPixels

class NotesAdapter(
    private val onNoteClickListener: ((NoteItemUi) -> Unit)? = null,
    private val onNoteLongClickListener: ((NoteItemUi) -> Unit)? = null,
) : ListAdapter<NoteItemUi, NotesAdapter.NoteViewHolder>(DIFF_UTIL_ITEM_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding, onNoteClickListener, onNoteLongClickListener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    companion object {
        private val DIFF_UTIL_ITEM_CALLBACK = object : DiffUtil.ItemCallback<NoteItemUi>() {
            override fun areItemsTheSame(oldItem: NoteItemUi, newItem: NoteItemUi): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NoteItemUi, newItem: NoteItemUi): Boolean =
                oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.content == newItem.content && oldItem.isSelected == newItem.isSelected
        }
    }

    class NoteViewHolder(
        private val binding: ItemNoteBinding,
        private val onNoteClickListener: ((NoteItemUi) -> Unit)?,
        private val onNoteLongClickListener: ((NoteItemUi) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteItemUi) {
            with(binding) {
                tvNoteTitle.text = note.title
                tvNoteContent.text = note.content

                card.strokeWidth = card.context.dpToPixels(
                    if (note.isSelected) CARD_STROKE_SELECTED_WIDTH else CARD_STROKE_NOT_SELECTED_WIDTH
                )

                card.setOnClickListener {
                    onNoteClickListener?.invoke(note)
                }
                card.setOnLongClickListener {
                    onNoteLongClickListener?.invoke(note)
                    true
                }
            }
        }

        companion object {
            private const val CARD_STROKE_SELECTED_WIDTH = 3
            private const val CARD_STROKE_NOT_SELECTED_WIDTH = 1
        }
    }
}