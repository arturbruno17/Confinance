package me.arturbruno.confinance.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import me.arturbruno.confinance.database.entities.CardPurchase

@Dao
interface CardPurchaseHistoryDao {
    @Insert
    suspend fun insertCardPurchase(cardPurchase: CardPurchase)

    @Delete
    suspend fun deleteCardPurchase(cardPurchase: CardPurchase)
}
