package me.arturbruno.confinance

import java.text.NumberFormat

fun getCurrencySymbol(): String {
    val numberFormat = NumberFormat.getCurrencyInstance()
    val currency = numberFormat.currency?.symbol
    return currency ?: "$"
}