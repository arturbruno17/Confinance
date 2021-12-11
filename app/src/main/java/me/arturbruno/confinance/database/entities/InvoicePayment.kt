package me.arturbruno.confinance.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoice_payment")
data class InvoicePayment(
    @PrimaryKey val id: Long,
    val value: Double,
    val date: String,
    @ColumnInfo(name = "card_id") val cardId: Long,
    @ColumnInfo(name = "account_id") val accountId: Long,
)
