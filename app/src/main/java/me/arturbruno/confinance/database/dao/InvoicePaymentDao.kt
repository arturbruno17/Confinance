package me.arturbruno.confinance.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import me.arturbruno.confinance.database.entities.InvoicePayment

@Dao
interface InvoicePaymentDao {
    @Insert
    suspend fun insertInvoicePayment(invoicePayment: InvoicePayment)

    @Delete
    suspend fun deleteInvoicePayment(invoicePayment: InvoicePayment)
}
