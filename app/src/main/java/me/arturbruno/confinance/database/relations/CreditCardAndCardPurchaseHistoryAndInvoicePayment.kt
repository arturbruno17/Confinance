package me.arturbruno.confinance.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import me.arturbruno.confinance.database.entities.CardPurchase
import me.arturbruno.confinance.database.entities.CreditCard
import me.arturbruno.confinance.database.entities.InvoicePayment

data class CreditCardAndCardPurchaseHistoryAndInvoicePayment(
    @Embedded val creditCard: CreditCard,
    @Relation(
        parentColumn = "id",
        entityColumn = "cardId"
    )
    val cardPurchaseHistory: List<CardPurchase>?,
    @Relation(
        parentColumn = "id",
        entityColumn = "cardId"
    )
    val invoicePayment: List<InvoicePayment>?
)
