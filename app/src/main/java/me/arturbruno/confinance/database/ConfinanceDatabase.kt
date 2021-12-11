package me.arturbruno.confinance.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.arturbruno.confinance.database.dao.*
import me.arturbruno.confinance.database.entities.*

@Database(
    entities = [BankAccount::class, BankTransaction::class, CardPurchase::class,
        CreditCard::class, InvoicePayment::class], version = 1, exportSchema = false
)
abstract class ConfinanceDatabase : RoomDatabase() {
    abstract fun creditCardDao(): CreditCardDao
    abstract fun cardPurchaseHistoryDao(): CardPurchaseHistoryDao
    abstract fun bankTransactionHistoryDao(): BankTransactionHistoryDao
    abstract fun bankAccountDao(): BankAccountDao
    abstract fun invoicePaymentDao(): InvoicePaymentDao

    companion object {

        @Volatile
        private var INSTANCE: ConfinanceDatabase? = null

        fun getDatabase(context: Context): ConfinanceDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConfinanceDatabase::class.java,
                    "confinance_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}