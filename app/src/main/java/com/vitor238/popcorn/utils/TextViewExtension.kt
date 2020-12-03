package com.vitor238.popcorn.utils

import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.core.text.bold


fun TextView.setFormatedText(text: String) {
    val delimiter = ":"
    val string = SpannableStringBuilder()
        .bold { append(text.substringBefore(delimiter) + delimiter) }
        .append(text.substringAfter(delimiter))
    this.text = string
}