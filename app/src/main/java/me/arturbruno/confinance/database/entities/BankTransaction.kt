package me.arturbruno.confinance.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.arturbruno.confinance.models.BankTransaction

@Entity(tableName = "bank_transaction_history")
data class BankTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val value: Double,
    val date: String,
    @ColumnInfo(name = "bank_id") val bankId: Long
)

fun BankTransaction.asEntity() =
    me.arturbruno.confinance.database.entities.BankTransaction(
        id = id,
        name = name,
        value = value,
        date = date,
        bankId = bankId
    )