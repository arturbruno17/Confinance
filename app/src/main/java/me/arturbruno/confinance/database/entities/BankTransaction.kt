package me.arturbruno.confinance.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_transaction_history")
data class BankTransaction(
    @PrimaryKey val id: Long,
    val value: Double,
    val date: String,
    @ColumnInfo(name = "bank_id") val bankId: Long
)