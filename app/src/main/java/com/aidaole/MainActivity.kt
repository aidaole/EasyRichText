package com.aidaole

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
                changeButtonsState(selStart, selEnd)
            }
        })
        layout.boldBtn.setOnClickListener {
            if (layout.edittext.isSpan(BoldSpan(selectStart(), selectEnd()))) {
                layout.edittext.removeSpan(BoldSpan(selectStart(), selectEnd()))
                layout.boldBtn.setTextColor(Color.WHITE)
            } else {
                layout.edittext.addSpan(BoldSpan(selectStart(), selectEnd()))
            }
        }
        layout.textColorBtn.setOnClickListener {
            if (layout.textColorBtn.textColors.defaultColor == Color.GREEN) {
                layout.edittext.removeSpan(TextColorSpan(selectStart(), selectEnd()))
                layout.textColorBtn.setTextColor(Color.WHITE)
            } else {
                layout.edittext.addSpan(TextColorSpan(selectStart(), selectEnd()))
            }
        }
        layout.textItalicBtn.setOnClickListener {
            if (layout.textItalicBtn.textColors.defaultColor == Color.GREEN) {
                layout.edittext.removeSpan(ItalicSpan(selectStart(), selectEnd()))
                layout.textItalicBtn.setTextColor(Color.WHITE)
            } else {
                layout.edittext.addSpan(ItalicSpan(selectStart(), selectEnd()))
            }
        }
        layout.textUnderlineBtn.setOnClickListener {
            if (layout.textUnderlineBtn.textColors.defaultColor == Color.GREEN) {
                layout.edittext.removeSpan(UnderlineSpan(selectStart(), selectEnd()))
                layout.textUnderlineBtn.setTextColor(Color.WHITE)
            } else {
                layout.edittext.addSpan(UnderlineSpan(selectStart(), selectEnd()))
            }
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

    private fun changeButtonsState(selStart: Int, selEnd: Int) {
        if (layout.edittext.isSpan(BoldSpan(selStart, selEnd))) {
            layout.boldBtn.setTextColor(Color.GREEN)
        } else {
            layout.boldBtn.setTextColor(Color.WHITE)
        }
        if (layout.edittext.isSpan(TextColorSpan(selStart, selEnd))) {
            layout.textColorBtn.setTextColor(Color.GREEN)
        } else {
            layout.textColorBtn.setTextColor(Color.WHITE)
        }
        if (layout.edittext.isSpan(ItalicSpan(selStart, selEnd))) {
            layout.textItalicBtn.setTextColor(Color.GREEN)
        } else {
            layout.textItalicBtn.setTextColor(Color.WHITE)
        }
        if (layout.edittext.isSpan(UnderlineSpan(selStart, selEnd))) {
            layout.textUnderlineBtn.setTextColor(Color.GREEN)
        } else {
            layout.textUnderlineBtn.setTextColor(Color.WHITE)
        }
    }

    private fun selectStart() = layout.edittext.selectionStart
    private fun selectEnd() = layout.edittext.selectionEnd

}