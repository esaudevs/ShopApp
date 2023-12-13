package com.esaudev.shopapp.ext

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Double.formatAsMoney(currencyCode: String = "USD"): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    formatter.currency = Currency.getInstance(currencyCode)
    return formatter.format(this)
}