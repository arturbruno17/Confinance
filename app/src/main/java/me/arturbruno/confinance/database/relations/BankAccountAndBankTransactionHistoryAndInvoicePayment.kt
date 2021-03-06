package me.arturbruno.confinance.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import me.arturbruno.confinance.database.entities.BankAccount
import me.arturbruno.confinance.database.entities.BankTransaction
import me.arturbruno.confinance.database.entities.InvoicePayment

data class BankAccountAndBankTransactionHistoryAndInvoicePayment(
    @Embedded val bankAccount: BankAccount,
    @Relation(
        parentColumn = "id",
        entityColumn = "bank_id"
    )
    val bankTransactionHistory: List<BankTransaction>?,
    @Relation(
        parentColumn = "id",
        entityColumn = "account_id"
    )
    val invoicePayment: List<InvoicePayment>?
)