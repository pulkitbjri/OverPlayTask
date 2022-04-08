package com.example.overplaytask.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.toast(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) =
    showToast(this, text, duration)

fun FragmentActivity.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) =
    showToast(this, getString(resId), duration)

fun Fragment.toast(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    activity?.let {
        it.toast(text, duration)
    }
}

fun Fragment.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    activity?.let {
        it.toast(resId, duration)
    }
}
private fun showToast(context: Context, text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    val toast = Toast.makeText(context, text, duration)
    toast.show()
}
