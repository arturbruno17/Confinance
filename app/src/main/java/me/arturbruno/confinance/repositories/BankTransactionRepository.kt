package me.arturbruno.confinance.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.arturbruno.confinance.database.ConfinanceDatabase
import me.arturbruno.confinance.database.entities.BankTransaction
import me.arturbruno.confinance.database.entities.CardPurchase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BankTransactionRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val database: ConfinanceDatabase
) {

    suspend fun insertBankTransaction(bankTransaction: BankTransaction) {
        withContext(ioDispatcher) {
            database.bankTransactionHistoryDao().insertBankTransaction(bankTransaction)
        }
    }

    suspend fun getAllBankTransactions() = withContext(ioDispatcher) {
        database.bankTransactionHistoryDao().getAllBankTransactions()
    }

    suspend fun deleteBankTransaction(bankTransaction: BankTransaction) {
        withContext(ioDispatcher) {
            database.bankTransactionHistoryDao().deleteBankTransaction(bankTransaction)
        }
    }

    suspend fun deleteBankTransactionsByBankId(bankId: Long) {
        withContext(ioDispatcher) {
            database.bankTransactionHistoryDao().deleteBankTransactionsByBankId(bankId)
        }
    }

}