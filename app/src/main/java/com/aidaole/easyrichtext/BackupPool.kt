package com.aidaole.easyrichtext

import android.text.SpannableStringBuilder
import android.widget.EditText
import com.aidaole.logi
import java.util.LinkedList

class BackupPool(private val maxSize: Int) {
    companion object {
        private const val TAG = "BackupPool"
    }

    private val backPoll = LinkedList<BackupItem>()

    fun push(item: BackupItem) {
        if (backPoll.size == maxSize) {
            backPoll.removeFirst()
            "push-> 已经满了，删除第一个".logi(TAG)
        }
        backPoll.addLast(item)
    }

    fun pop(): BackupItem? {
        if (maxSize == 0) {
            "pop-> 已经没有备份了".logi(TAG)
        } else {
            return backPoll.removeLastOrNull()
        }
        return null
    }

    fun clear() {
        backPoll.clear()
    }
}

class BackupItem(
    val spannableStringBuilder: SpannableStringBuilder,
    val selectStart: Int,
    val selectEnd: Int,
)

fun EditText.createBackupItem(): BackupItem {
    return BackupItem(
        SpannableStringBuilder(this.text),
        this.selectionStart,
        this.selectionEnd
    )
}