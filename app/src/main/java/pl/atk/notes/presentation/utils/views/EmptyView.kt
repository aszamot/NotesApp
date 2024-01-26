package pl.atk.notes.presentation.utils.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import pl.atk.notes.R
import pl.atk.notes.databinding.ViewEmptyBinding

class EmptyView(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    private val binding: ViewEmptyBinding =
        ViewEmptyBinding.inflate(LayoutInflater.from(context), this)

    private var text: String? = null
    private var icon: Drawable? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.EmptyView, 0, 0).apply {
            try {
                text = getString(R.styleable.EmptyView_text)
                icon = getDrawable(R.styleable.EmptyView_icon)
            } finally {
                recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setText()
        setIcon()
    }

    private fun setText() {
        binding.tvMessage.text = text
    }

    private fun setIcon() {
        binding.ivIcon.setImageDrawable(icon)
    }
}