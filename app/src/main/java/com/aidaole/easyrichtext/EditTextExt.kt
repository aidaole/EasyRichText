package com.aidaole.easyrichtext


import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.widget.EditText
import androidx.core.text.getSpans
import com.aidaole.logi

class EditableExt

private const val TAG = "EditableExt"

fun EditText.spanBuilder() = SpannableStringBuilder(this.text)

fun EditText.printSpans(start: Int, end: Int) {
    val spannableString = SpannableStringBuilder(this.text)
    spannableString.getSpans<CharacterStyle>(0, this.length()).forEach {
        when (it) {
            is StyleSpan -> {
            }
        }
        val spanStart = spannableString.getSpanStart(it)
        val spanEnd = spannableString.getSpanEnd(it)
        "printSpans-> ${it}, (${spanStart}, ${spanEnd})".logi(TAG)
    }
}

