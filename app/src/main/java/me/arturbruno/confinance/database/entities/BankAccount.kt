package me.arturbruno.confinance.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_account")
data class BankAccount(
    @PrimaryKey val id: Long,
    val name: String,
    val type: AccountType,
    val bank: String,
    val balance: Double
)

enum class AccountType {
    CHECKING_ACCOUNT,
    SAVINGS_ACCOUNT
}
