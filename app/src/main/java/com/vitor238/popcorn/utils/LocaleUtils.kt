package com.vitor238.popcorn.utils

import java.util.*

object LocaleUtils {
    fun getLanguage(): String {
        return Locale.getDefault().toLanguageTag()
    }

    fun getCountry(): String {
        return Locale.getDefault().country
    }
}