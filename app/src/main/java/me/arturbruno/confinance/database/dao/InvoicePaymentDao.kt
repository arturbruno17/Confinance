package me.arturbruno.confinance.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.arturbruno.confinance.database.entities.InvoicePayment

@Dao
interface InvoicePaymentDao {
    @Insert
    suspend fun insertInvoicePayment(invoicePayment: InvoicePayment)

    @Delete
    suspend fun deleteInvoicePayment(invoicePayment: InvoicePayment)

    @Query("SELECT * FROM invoice_payment")
    fun getAllInvoicePayments(): Flow<List<InvoicePayment>>

    @Query("DELETE FROM invoice_payment WHERE card_id = :cardId")
    suspend fun deleteInvoicePaymentsByCardId(cardId: Long)
}
