package me.arturbruno.confinance.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.arturbruno.confinance.database.entities.CreditCard
import me.arturbruno.confinance.database.relations.CreditCardAndCardPurchaseHistoryAndInvoicePayment

@Dao
interface CreditCardDao {
    @Insert
    suspend fun insertCreditCard(creditCard: CreditCard)

    @Delete
    suspend fun deleteCreditCard(creditCard: CreditCard)

    @Update
    suspend fun updateCreditCard(creditCard: CreditCard)

    @Query("SELECT * FROM credit_card")
    fun getAllCreditCards(): Flow<CreditCard>

    @Query("SELECT * FROM credit_card WHERE id = :cardId")
    @Transaction
    fun getAllCreditCardsWithTransactions(cardId: Long): Flow<CreditCardAndCardPurchaseHistoryAndInvoicePayment>
}
