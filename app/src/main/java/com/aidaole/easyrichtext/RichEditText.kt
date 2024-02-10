package com.aidaole.easyrichtext

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.aidaole.logi

class RichEditText : AppCompatEditText {
    companion object {
        private const val TAG = "RichEditText"
    }

    private var selectionChangedListener: OnSelectionChangedListener? = null

    interface OnSelectionChangedListener {
        fun onSelectionChanged(selStart: Int, selEnd: Int)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                "beforeTextChanged-> isFromBack: ${isFromBack}, fromForward: ${isFromForward}, $s, $start, $count, $after".logi(
                    TAG
                )
                if (!isFromBack && !isFromForward && count != after) {
                    // 不是来自于前进或者后退，并且有内容变化，才需要入回退栈
                    backupPool.push(createBackupItem())
                    forwardPoll.clear()
                }
                isFromBack = false
                isFromForward = false
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
    }

    fun setOnSelectionChangedListener(listener: OnSelectionChangedListener) {
        this.selectionChangedListener = listener
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        selectionChangedListener?.onSelectionChanged(selStart, selEnd)
    }

    /**
     * 判断是否选中的区域是某一种span
     *
     * @param spanStyle
     * @return
     */
    fun isSpan(spanStyle: AbstractSpan): Boolean {
        val allSpans = mutableListOf<AbstractSpan>()
        val sourceSpans =
            text!!.getSpans(spanStyle.start, spanStyle.end, spanStyle.getSourceSpanType()) ?: emptyArray()
        sourceSpans.forEach {
            val start = text!!.getSpanStart(it)
            val end = text!!.getSpanEnd(it)
            if (spanStyle.isType(it)) {
                allSpans.add(spanStyle.create(start, end))
            }
        }
        allSpans.forEach {
            if (it.start <= spanStyle.start && it.end >= spanStyle.end) {
                return true
            }
        }
        return false
    }

    private val backupPool = BackupPool(5)
    private val forwardPoll = BackupPool(5)

    /**
     * 添加一个span
     *
     * @param span
     */
    fun addSpan(span: AbstractSpan) {
        backupPool.push(createBackupItem())
        forwardPoll.clear()
        this.text = span.add(this)
        this.mergeSpans(span)
        this.setSelection(span.start)
    }

    /**
     * 删除span
     *
     * @param span
     */
    fun removeSpan(span: AbstractSpan) {
        val result = getAllAbsSpans(span)
        val allSpans = result.first
        if (allSpans.size != 1) {
            "removeSpan-> 状态有问题".logi(TAG)
            return
        } else {
            backupPool.push(createBackupItem())
            forwardPoll.clear()
            text!!.removeSpan(result.second[0])
            if (span.start == span.end) {
                "removeSpan-> 选中为0，直接删除整个span".logi(TAG)
            } else {
                val modifySpan = allSpans[0]
                val section1 = Pair(modifySpan.start, span.start)
                val section2 = Pair(span.end, modifySpan.end)
                if (section1.first != section1.second) {
                    addSpan(span.create(section1.first, section1.second))
                }
                if (section2.first != section2.second) {
                    addSpan(span.create(section2.first, section2.second))
                }
            }
        }
        this.setSelection(span.start)
    }

    private var isFromBack = false
    private var isFromForward = false

    fun back() {
        val item = backupPool.pop()
        item?.let {
            forwardPoll.push(createBackupItem())
            isFromBack = true
            this.text = it.spannableStringBuilder
            this.setSelection(it.selectStart, it.selectStart)
        } ?: run {
            "back-> 没有可以回退的item".logi(TAG)
        }
    }

    fun forward() {
        val item = forwardPoll.pop()
        item?.let {
            backupPool.push(createBackupItem())
            isFromForward = true
            this.text = it.spannableStringBuilder
            this.setSelection(it.selectStart, it.selectEnd)
        } ?: run {
            "forward-> 没有可以前进的item".logi(TAG)
        }
    }

    private fun mergeSpans(span: AbstractSpan) {
        val result = getAllAbsSpans(span)
        var allSpans = result.first
        result.second.forEach {
            text!!.removeSpan(it)
        }
        var i = 0
        var j = 0
        val newSpans = mutableListOf<AbstractSpan>()
        allSpans = allSpans.sortedWith { o1, o2 ->
            if (o1.start != o2.start) {
                o1.start - o2.start
            } else if (o1.end != o2.end) {
                o1.end - o2.end
            } else {
                0
            }
        }
        if (allSpans.isNotEmpty()) {
            newSpans.add(allSpans[i++])
        }
        while (i < allSpans.size) {
            val cur = newSpans[j]
            if (allSpans[i].start > cur.end) {
                newSpans.add(span.create(allSpans[i].start, allSpans[i].end))
                j++
            } else {
                newSpans.removeAt(j)
                newSpans.add(
                    span.create(
                        Math.min(cur.start, allSpans[i].start),
                        Math.max(cur.end, allSpans[i].end)
                    )
                )
            }
            i++
        }
        newSpans.forEach {
            text = it.add(this)
        }
    }

    private fun getAllAbsSpans(span: AbstractSpan): Pair<List<AbstractSpan>, List<Any>> {
        val allSpans = mutableListOf<AbstractSpan>()
        val source = mutableListOf<Any>()
        val allSourceSpans = text!!.getSpans(span.start, span.end, span.getSourceSpanType())
        allSourceSpans.forEach {
            val start = text!!.getSpanStart(it)
            val end = text!!.getSpanEnd(it)
            if (span.isType(it)) {
                allSpans.add(span.create(start, end))
                source.add(it)
            }
        }
        return Pair(allSpans, source)
    }
}