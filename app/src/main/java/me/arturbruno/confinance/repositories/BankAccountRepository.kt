package me.arturbruno.confinance.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.arturbruno.confinance.database.ConfinanceDatabase
import me.arturbruno.confinance.database.entities.BankAccount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BankAccountRepository @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val database: ConfinanceDatabase
) {
    suspend fun insertBankAccount(bankAccount: BankAccount) {
        withContext(defaultDispatcher) {
            database.bankAccountDao().insertBankAccount(bankAccount)
        }
    }

    suspend fun getAllBankAccounts() = withContext(defaultDispatcher) {
        database.bankAccountDao().getAllBankAccounts()
    }

    suspend fun deleteBankAccount(bankId: Long) {
        withContext(defaultDispatcher) {
            database.bankAccountDao().deleteBankAccount(bankId)
        }
    }

    suspend fun getBankAccountWithTransactions(bankId: Long) =
        withContext(defaultDispatcher) {
            database.bankAccountDao().getBankAccountWithTransactions(bankId)
        }
}
