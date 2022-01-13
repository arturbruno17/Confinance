package me.arturbruno.confinance.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.arturbruno.confinance.database.entities.BankAccount
import me.arturbruno.confinance.database.relations.BankAccountAndBankTransactionHistoryAndInvoicePayment

@Dao
interface BankAccountDao {
    @Insert
    suspend fun insertBankAccount(bankAccount: BankAccount)

    @Query("DELETE FROM bank_account WHERE id = :bankId")
    suspend fun deleteBankAccount(bankId: Long)

    @Update
    suspend fun updateBankAccount(bankAccount: BankAccount)

    @Query("SELECT * FROM bank_account")
    fun getAllBankAccounts(): Flow<List<BankAccount>>

    @Query("SELECT * FROM bank_account WHERE id = :bankId")
    @Transaction
    fun getBankAccountWithTransactions(bankId: Long): Flow<BankAccountAndBankTransactionHistoryAndInvoicePayment>
}
