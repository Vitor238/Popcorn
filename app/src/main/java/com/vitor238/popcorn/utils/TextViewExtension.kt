package com.vitor238.popcorn.utils

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.text.bold


fun TextView.setDetails(@StringRes stringResId: Int, data: String?) {
    if (data.isNullOrBlank()) {
        this.visibility = View.GONE
    } else {
        val text = this.context.getString(stringResId, data)
        val delimiter = ":"
        val string = SpannableStringBuilder()
            .bold { append(text.substringBefore(delimiter) + delimiter) }
            .append(text.substringAfter(delimiter))
        this.text = string
    }
}

fun TextView.setDetails(@StringRes stringResId: Int, data: Int?) {
    if (data == null) {
        this.visibility = View.GONE
    } else {
        val text = this.context.getString(stringResId, data)
        val delimiter = ":"
        val string = SpannableStringBuilder()
            .bold { append(text.substringBefore(delimiter) + delimiter) }
            .append(text.substringAfter(delimiter))
        this.text = string
    }
}