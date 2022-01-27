package me.arturbruno.confinance.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.arturbruno.confinance.database.ConfinanceDatabase
import me.arturbruno.confinance.database.entities.CardPurchase
import me.arturbruno.confinance.database.entities.InvoicePayment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvoicePaymentRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val database: ConfinanceDatabase
) {

    suspend fun insertInvoicePayment(invoicePayment: InvoicePayment) {
        withContext(ioDispatcher) {
            database.invoicePaymentDao().insertInvoicePayment(invoicePayment)
        }
    }

    suspend fun getAllInvoicePayments() = withContext(ioDispatcher) {
        database.invoicePaymentDao().getAllInvoicePayments()
    }

    suspend fun deleteInvoicePayment(invoicePayment: InvoicePayment) {
        withContext(ioDispatcher) {
            database.invoicePaymentDao().deleteInvoicePayment(invoicePayment)
        }
    }

    suspend fun deleteInvoicePaymentsByCardId(cardId: Long) {
        withContext(ioDispatcher) {
            database.invoicePaymentDao().deleteInvoicePaymentsByCardId(cardId)
        }
    }

}
