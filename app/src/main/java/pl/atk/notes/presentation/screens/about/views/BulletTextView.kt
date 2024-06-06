package pl.atk.notes.presentation.screens.about.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import pl.atk.notes.R
import pl.atk.notes.databinding.ViewBulletTextBinding

class BulletTextView(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {
    private val binding: ViewBulletTextBinding =
        ViewBulletTextBinding.inflate(LayoutInflater.from(context), this)

    private var text: String? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.BulletTextView, 0, 0).apply {
            try {
                text = getString(R.styleable.BulletTextView_text)
            } finally {
                recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setText()
    }

    private fun setText() {
        binding.tvText.text =
            text?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
    }
}