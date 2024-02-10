package com.aidaole.easyrichtext

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.EditText

class ItalicSpan(start: Int, end: Int) : AbstractSpan(start, end) {
    override fun add(editText: EditText): SpannableStringBuilder {
        return editText.spanBuilder().apply {
            setSpan(StyleSpan(Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    override fun create(start: Int, end: Int): AbstractSpan {
        return ItalicSpan(start, end)
    }

    override fun getSourceSpanType(): Class<out Any> {
        return StyleSpan::class.java
    }

    override fun isType(span: Any): Boolean {
        return span::class.java == getSourceSpanType()
                && (span as StyleSpan).style == Typeface.ITALIC
    }
}