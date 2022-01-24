package me.arturbruno.confinance.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.arturbruno.confinance.database.entities.BankTransaction
import me.arturbruno.confinance.database.entities.InvoicePayment

@Dao
interface BankTransactionHistoryDao {
    @Insert
    suspend fun insertBankTransaction(bankTransaction: BankTransaction)

    @Delete
    suspend fun deleteBankTransaction(bankTransaction: BankTransaction)

    @Query("SELECT * FROM bank_transaction_history")
    fun getAllBankTransactions(): Flow<List<BankTransaction>>
}
