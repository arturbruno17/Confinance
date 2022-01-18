package me.arturbruno.confinance.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.arturbruno.confinance.models.CardPurchase

@Entity(tableName = "card_purchase_history")
data class CardPurchase(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val value: Double,
    val date: String,
    @ColumnInfo(name = "card_id") val cardId: Long
)

fun CardPurchase.asEntity() =
    me.arturbruno.confinance.database.entities.CardPurchase(
        id = id,
        name = name,
        value = value,
        date = date,
        cardId = cardId
    )