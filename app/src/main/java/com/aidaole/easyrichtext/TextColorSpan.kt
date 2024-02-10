package com.aidaole.easyrichtext

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.EditText

class TextColorSpan(start: Int, end: Int) : AbstractSpan(start, end) {
    override fun add(editText: EditText): SpannableStringBuilder {
        return editText.spanBuilder().apply {
            setSpan(ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    override fun create(start: Int, end: Int): AbstractSpan {
        return TextColorSpan(start, end)
    }

    override fun getSourceSpanType(): Class<out Any> {
        return ForegroundColorSpan::class.java
    }

    override fun isType(span: Any): Boolean {
        return span::class.java == ForegroundColorSpan::class.java
    }
}