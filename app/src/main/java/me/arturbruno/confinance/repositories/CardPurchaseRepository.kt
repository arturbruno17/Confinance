package me.arturbruno.confinance.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.arturbruno.confinance.database.ConfinanceDatabase
import me.arturbruno.confinance.database.entities.CardPurchase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardPurchaseRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val database: ConfinanceDatabase
) {

    suspend fun insertCardPurchase(cardPurchase: CardPurchase) {
        withContext(ioDispatcher) {
            database.cardPurchaseHistoryDao().insertCardPurchase(cardPurchase)
        }
    }

}