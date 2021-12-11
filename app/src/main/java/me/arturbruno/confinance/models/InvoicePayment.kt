package me.arturbruno.confinance.models

import me.arturbruno.confinance.database.entities.CreditCard
import me.arturbruno.confinance.database.entities.InvoicePayment

data class InvoicePayment(
    val id: Long,
    val value: Double,
    val date: String,
    val cardId: Long,
    val accountId: Long,
)

fun InvoicePayment.asModel() =
    me.arturbruno.confinance.models.InvoicePayment(
        id = id,
        value = value,
        date = date,
        cardId = cardId,
        accountId = accountId
    )
