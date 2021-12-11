package me.arturbruno.confinance.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_purchase_history")
data class CardPurchase(
    @PrimaryKey val id: Long,
    val value: Double,
    val date: String,
    @ColumnInfo(name = "card_id") val cardId: Long
)
