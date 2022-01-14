package me.arturbruno.confinance.models

import me.arturbruno.confinance.database.entities.BankTransaction

data class BankTransaction(
    val id: Long,
    val name: String,
    val value: Double,
    val date: String,
    val bankId: Long
)

fun BankTransaction.asModel() =
    me.arturbruno.confinance.models.BankTransaction(
        id = id,
        name = name,
        value = value,
        date = date,
        bankId = bankId
    )