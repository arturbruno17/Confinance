package me.arturbruno.confinance.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.arturbruno.confinance.database.entities.BankAccount
import me.arturbruno.confinance.database.relations.BankAccountAndBankTransactionHistoryAndInvoicePayment

@Dao
interface BankAccountDao {
    @Insert
    suspend fun insertBankAccount(bankAccountDao: BankAccountDao)

    @Delete
    suspend fun deleteBankAccount(bankAccountDao: BankAccountDao)

    @Update
    suspend fun updateBankAccount(bankAccountDao: BankAccountDao)

    @Query("SELECT * FROM bank_account")
    fun getAllBankAccounts(): Flow<BankAccount>

    @Query("SELECT * FROM bank_account WHERE id = :bankId")
    fun getBankAccountWithTransactions(bankId: Long): Flow<BankAccountAndBankTransactionHistoryAndInvoicePayment>
}
