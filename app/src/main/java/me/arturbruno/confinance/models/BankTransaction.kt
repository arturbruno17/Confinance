package me.arturbruno.confinance.models

import me.arturbruno.confinance.database.entities.BankTransaction

data class BankTransaction(
    val id: Long,
    val value: Double,
    val date: String,
    val bankId: Long
)

fun BankTransaction.asModel() =
    me.arturbruno.confinance.models.BankTransaction(
        id = id,
        value = value,
        date = date,
        bankId = bankId
    )