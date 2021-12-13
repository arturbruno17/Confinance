package me.arturbruno.confinance.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.arturbruno.confinance.database.ConfinanceDatabase
import me.arturbruno.confinance.database.entities.BankAccount

class BankAccountRepository(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val database: ConfinanceDatabase
) {
    suspend fun insertBankAccount(bankAccount: BankAccount) {
        withContext(defaultDispatcher) {
            database.bankAccountDao().insertBankAccount(bankAccount)
        }
    }
}
