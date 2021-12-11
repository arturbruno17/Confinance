package me.arturbruno.confinance.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credit_card")
data class CreditCard(
    @PrimaryKey val id: Long,
    val name: String,
    val limit: Double,
    @ColumnInfo(name = "invoice_due_date") val invoiceDueDate: String,
    val bank: String,
    val invoice: Double
)
