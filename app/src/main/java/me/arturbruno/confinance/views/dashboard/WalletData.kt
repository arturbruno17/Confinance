package me.arturbruno.confinance.views.dashboard

import me.arturbruno.confinance.models.BankAccount
import me.arturbruno.confinance.models.CreditCard

sealed class WalletData {
    data class BankAccountItem(val data: BankAccount) : WalletData()
    data class CreditCardItem(val data: CreditCard) : WalletData()
}
