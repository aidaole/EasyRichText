package com.aidaole.easyrichtext


import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.widget.EditText
import androidx.core.text.getSpans
import com.aidaole.logi

class EditableExt

private const val TAG = "EditableExt"

fun EditText.spanBuilder() = SpannableStringBuilder(this.text)

fun EditText.printSpans(start: Int = 0, end: Int = this.length()) {
    val spannableString = SpannableStringBuilder(this.text)
    var atLeast = 0
    spannableString.getSpans<CharacterStyle>(start, end).forEach {
        val spanStart = spannableString.getSpanStart(it)
        val spanEnd = spannableString.getSpanEnd(it)
        "printSpans-> ${it}, (${spanStart}, ${spanEnd})".logi(TAG)
        atLeast++
    }
    if (atLeast == 0) {
        "printSpans-> 没有任何span".logi(TAG)
    }
}

