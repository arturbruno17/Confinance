package me.arturbruno.confinance.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.arturbruno.confinance.database.ConfinanceDatabase
import me.arturbruno.confinance.database.entities.BankTransaction
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

}