package me.arturbruno.confinance.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.arturbruno.confinance.models.CreditCard

@Entity(tableName = "credit_card")
data class CreditCard(
    @PrimaryKey val id: Long,
    val name: String,
    val limit: Double,
    @ColumnInfo(name = "invoice_due_date") val invoiceDueDate: String,
    val bank: String,
    val invoice: Double
)

fun CreditCard.asEntity() =
    me.arturbruno.confinance.database.entities.CreditCard(
        id = id,
        name = name,
        limit = limit,
        invoiceDueDate = invoiceDueDate,
        bank = bank,
        invoice = invoice
    )