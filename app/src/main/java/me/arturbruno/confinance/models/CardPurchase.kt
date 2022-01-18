package me.arturbruno.confinance.models

import me.arturbruno.confinance.database.entities.CardPurchase

data class CardPurchase(
    val id: Long,
    val name: String,
    val value: Double,
    val date: String,
    val cardId: Long
)

fun CardPurchase.asModel() =
    me.arturbruno.confinance.models.CardPurchase(
        id = id,
        name = name,
        value = value,
        date = date,
        cardId = cardId
    )