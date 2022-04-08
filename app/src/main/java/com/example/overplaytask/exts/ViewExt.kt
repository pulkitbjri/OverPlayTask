package com.example.overplaytask.exts

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.getSystemService

fun EditText.requestFocusWithKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    requestFocus()
}

fun EditText.hideKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Activity.hideKeyboard() {
    currentFocus?.also { view ->
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
