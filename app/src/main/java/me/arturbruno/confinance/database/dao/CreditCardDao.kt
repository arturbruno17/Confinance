package me.arturbruno.confinance.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.arturbruno.confinance.database.entities.CreditCard
import me.arturbruno.confinance.database.relations.CreditCardAndCardPurchaseHistoryAndInvoicePayment

@Dao
interface CreditCardDao {
    @Insert
    suspend fun insertCreditCard(creditCard: CreditCard)

    @Query("DELETE FROM credit_card WHERE id = :cardId")
    suspend fun deleteCreditCard(cardId: Long)

    @Update
    suspend fun updateCreditCard(creditCard: CreditCard)

    @Query("SELECT * FROM credit_card")
    fun getAllCreditCards(): Flow<List<CreditCard>>

    @Query("SELECT * FROM credit_card WHERE id = :cardId")
    @Transaction
    fun getAllCreditCardsWithTransactions(cardId: Long): Flow<CreditCardAndCardPurchaseHistoryAndInvoicePayment>

    @Query("UPDATE credit_card SET invoice = invoice + :value WHERE id = :id")
    suspend fun updateCreditCardInvoiceById(id: Long, value: Double)
}
