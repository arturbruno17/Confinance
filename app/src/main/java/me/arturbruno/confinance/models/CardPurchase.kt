package me.arturbruno.confinance.models

import me.arturbruno.confinance.database.entities.CardPurchase

data class CardPurchase(
    val id: Long,
    val value: Double,
    val date: String,
    val cardId: Long
)

fun CardPurchase.asModel() =
    me.arturbruno.confinance.models.CardPurchase(
        id = id,
        value = value,
        date = date,
        cardId = cardId
    )