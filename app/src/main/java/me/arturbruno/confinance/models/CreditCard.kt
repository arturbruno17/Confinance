package me.arturbruno.confinance.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import me.arturbruno.confinance.database.entities.CreditCard

@Parcelize
data class CreditCard(
    val id: Long,
    val name: String,
    val limit: Double,
    val invoiceDueDate: String,
    val bank: String,
    val invoice: Double
): Parcelable

fun CreditCard.asModel() =
    me.arturbruno.confinance.models.CreditCard(
        id = id,
        name = name,
        limit = limit,
        invoiceDueDate = invoiceDueDate,
        bank = bank,
        invoice = invoice
    )