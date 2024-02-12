package com.aidaole

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.aidaole.easyrichtext.AbstractSpan
import com.aidaole.easyrichtext.BoldSpan
import com.aidaole.easyrichtext.ItalicSpan
import com.aidaole.easyrichtext.RichEditText
import com.aidaole.easyrichtext.TextColorSpan
import com.aidaole.easyrichtext.UnderlineSpan
import com.aidaole.easyrichtext.databinding.ActivityEditBinding
import com.aidaole.easyrichtext.printSpans

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "EditActivity"
    }

    private val layout by lazy { ActivityEditBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.root)

        layout.edittext.setOnSelectionChangedListener(object : RichEditText.OnSelectionChangedListener {
            override fun onSelectionChanged(selStart: Int, selEnd: Int) {
                "onSelectionChanged-> start: $selStart, end: $selEnd".logi(TAG)
                notifyBtnsStateChange(selStart, selEnd)
            }
        })


        layout.textBoldBtn.setOnClickListener {
            toggleSpan(BoldSpan(selectStart(), selectEnd()))
            notifyBtnsStateChange(selectStart(), selectEnd())
        }
        layout.textColorBtn.setOnClickListener {
            toggleSpan(TextColorSpan(selectStart(), selectEnd()))
            notifyBtnsStateChange(selectStart(), selectEnd())
        }
        layout.textItalicBtn.setOnClickListener {
            toggleSpan(ItalicSpan(selectStart(), selectEnd()))
            notifyBtnsStateChange(selectStart(), selectEnd())
        }
        layout.textUnderlineBtn.setOnClickListener {
            toggleSpan(UnderlineSpan(selectStart(), selectEnd()))
            notifyBtnsStateChange(selectStart(), selectEnd())
        }
        layout.printSpans.setOnClickListener {
            layout.edittext.printSpans(selectStart(), selectEnd())
        }
        layout.back.setOnClickListener {
            layout.edittext.back()
        }
        layout.forward.setOnClickListener {
            layout.edittext.forward()
        }
    }

    private fun toggleSpan(span: AbstractSpan) {
        if (layout.edittext.isSpan(span)) {
            layout.edittext.removeSpan(span)
        } else {
            layout.edittext.addSpan(span)
        }
    }

    private fun notifyBtnsStateChange(selStart: Int, selEnd: Int) {
        layout.textBoldBtn.toActive(layout.edittext.isSpan(BoldSpan(selStart, selEnd)))
        layout.textColorBtn.toActive(layout.edittext.isSpan(TextColorSpan(selStart, selEnd)))
        layout.textItalicBtn.toActive(layout.edittext.isSpan(ItalicSpan(selStart, selEnd)))
        layout.textUnderlineBtn.toActive(layout.edittext.isSpan(UnderlineSpan(selStart, selEnd)))
    }

    private fun selectStart() = layout.edittext.selectionStart
    private fun selectEnd() = layout.edittext.selectionEnd

    private fun Button.toActive(isActive: Boolean) {
        if (isActive) {
            this.setTextColor(Color.BLUE)
        } else {
            this.setTextColor(Color.WHITE)
        }
    }
}