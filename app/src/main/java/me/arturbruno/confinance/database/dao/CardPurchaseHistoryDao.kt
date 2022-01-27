package me.arturbruno.confinance.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.arturbruno.confinance.database.entities.CardPurchase
import me.arturbruno.confinance.database.entities.InvoicePayment

@Dao
interface CardPurchaseHistoryDao {
    @Insert
    suspend fun insertCardPurchase(cardPurchase: CardPurchase)

    @Delete
    suspend fun deleteCardPurchase(cardPurchase: CardPurchase)

    @Query("SELECT * FROM card_purchase_history")
    fun getAllCardPurchases(): Flow<List<CardPurchase>>

    @Query("DELETE FROM card_purchase_history WHERE card_id = :cardId")
    suspend fun deleteCardPurchasesByCardId(cardId: Long)
}
