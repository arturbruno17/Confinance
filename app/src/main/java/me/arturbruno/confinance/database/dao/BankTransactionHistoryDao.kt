package me.arturbruno.confinance.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import me.arturbruno.confinance.database.entities.BankTransaction

@Dao
interface BankTransactionHistoryDao {
    @Insert
    suspend fun insertBankTransaction(bankTransaction: BankTransaction)

    @Delete
    suspend fun deleteBankTransaction(bankTransaction: BankTransaction)
}
