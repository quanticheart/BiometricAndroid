package com.quanticheart.core.extentions

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.setFinishActionListener(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> callback()
        }
        false
    }
}