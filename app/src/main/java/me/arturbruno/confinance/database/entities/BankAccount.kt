package me.arturbruno.confinance.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.arturbruno.confinance.models.AccountType
import me.arturbruno.confinance.models.BankAccount

@Entity(tableName = "bank_account")
data class BankAccount(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val type: AccountType,
    val bank: String,
    val balance: Double
)

fun BankAccount.asEntity() =
    me.arturbruno.confinance.database.entities.BankAccount(
        id = id,
        name = name,
        type = type,
        bank = bank,
        balance = balance
    )