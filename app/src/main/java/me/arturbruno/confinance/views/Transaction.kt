package me.arturbruno.confinance.views

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import me.arturbruno.confinance.models.BankTransaction
import me.arturbruno.confinance.models.CardPurchase
import me.arturbruno.confinance.models.InvoicePayment

sealed class Transaction(val date: Long) {
    data class CardPurchaseItem(val data: CardPurchase) : Transaction(
        data.date.toLocalDateTime().toInstant(
            TimeZone.currentSystemDefault()
        ).toEpochMilliseconds()
    )

    data class BankTransactionItem(val data: BankTransaction) : Transaction(
        data.date.toLocalDateTime().toInstant(
            TimeZone.currentSystemDefault()
        ).toEpochMilliseconds()
    )

    data class InvoicePaymentItem(val data: InvoicePayment) : Transaction(
        data.date.toLocalDateTime().toInstant(
            TimeZone.currentSystemDefault()
        ).toEpochMilliseconds()
    )
}