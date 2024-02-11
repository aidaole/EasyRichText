package com.aidaole.easyrichtext

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.widget.EditText

class UnderlineSpan(start: Int, end: Int) : AbstractSpan(start, end) {
    override fun add(editText: EditText): SpannableStringBuilder {
        return editText.spanBuilder().apply {
            setSpan(android.text.style.UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    override fun create(start: Int, end: Int): AbstractSpan {
        return UnderlineSpan(start, end)
    }

    override fun getSourceSpanType(): Class<out Any> {
        return android.text.style.UnderlineSpan::class.java
    }

    override fun isType(span: Any): Boolean {
        return getSourceSpanType() == span::class.java
    }
}