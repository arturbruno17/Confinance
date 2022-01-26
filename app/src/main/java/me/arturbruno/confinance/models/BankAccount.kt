package me.arturbruno.confinance.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import me.arturbruno.confinance.database.entities.BankAccount

@Parcelize
data class BankAccount(
    val id: Long,
    val name: String,
    val type: AccountType,
    val bank: String,
    val balance: Double
) : Parcelable

fun BankAccount.asModel() =
    me.arturbruno.confinance.models.BankAccount(
        id = id,
        name = name,
        type = type,
        bank = bank,
        balance = balance
    )