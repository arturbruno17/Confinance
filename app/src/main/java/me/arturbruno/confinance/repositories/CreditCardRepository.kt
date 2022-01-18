package me.arturbruno.confinance.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.arturbruno.confinance.database.ConfinanceDatabase
import me.arturbruno.confinance.database.entities.CreditCard
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreditCardRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val database: ConfinanceDatabase
) {
    suspend fun insertCreditCard(creditCard: CreditCard) {
        withContext(ioDispatcher) {
            database.creditCardDao().insertCreditCard(creditCard)
        }
    }

    suspend fun updateCreditCard(creditCard: CreditCard) {
        withContext(ioDispatcher) {
            database.creditCardDao().updateCreditCard(creditCard)
        }
    }

    suspend fun getAllCreditCards() = withContext(ioDispatcher) {
        database.creditCardDao().getAllCreditCards()
    }

    suspend fun deleteCreditCard(cardId: Long) {
        withContext(ioDispatcher) {
            database.creditCardDao().deleteCreditCard(cardId)
        }
    }

    suspend fun getAllCreditCardsWithTransactions(cardId: Long) =
        withContext(ioDispatcher) {
            database.creditCardDao().getAllCreditCardsWithTransactions(cardId)
        }
}