package com.aidaole.easyrichtext

import android.text.SpannableStringBuilder
import android.widget.EditText

abstract class AbstractSpan(val start: Int, val end: Int) {

    abstract fun add(editText: EditText): SpannableStringBuilder

    abstract fun create(start: Int, end: Int): AbstractSpan

    abstract fun getSourceSpanType(): Class<out Any>

    abstract fun isType(span: Any): Boolean
}