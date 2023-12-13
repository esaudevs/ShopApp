package com.esaudev.shopapp.ext

import java.util.Locale

fun String.capitalizeFirstLetter(): String {
    if (isEmpty()) {
        return this
    }

    return this.substring(0,1).uppercase(Locale.ROOT) + this.substring(1)
}