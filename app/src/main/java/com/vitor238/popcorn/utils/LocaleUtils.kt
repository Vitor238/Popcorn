package com.vitor238.popcorn.utils

import java.util.*

object LocaleUtils {
    fun getLocale(): String {
        return Locale.getDefault().toLanguageTag()
    }
}