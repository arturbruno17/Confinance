package me.arturbruno.confinance.models

import me.arturbruno.confinance.database.entities.BankAccount

data class BankAccount(
    val id: Long,
    val name: String,
    val type: AccountType,
    val bank: String,
    val balance: Double
)

fun BankAccount.asModel() =
    me.arturbruno.confinance.models.BankAccount(
        id = id,
        name = name,
        type = type,
        bank = bank,
        balance = balance
    )