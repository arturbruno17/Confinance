package me.arturbruno.confinance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.arturbruno.confinance.database.entities.asEntity
import me.arturbruno.confinance.models.*
import me.arturbruno.confinance.repositories.BankAccountRepository
import me.arturbruno.confinance.repositories.CardPurchaseRepository
import me.arturbruno.confinance.repositories.CreditCardRepository
import me.arturbruno.confinance.repositories.InvoicePaymentRepository
import javax.inject.Inject

@HiltViewModel
class CreditCardDetailsViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
    private val cardPurchaseRepository: CardPurchaseRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val invoicePaymentRepository: InvoicePaymentRepository
) : ViewModel() {

    private var _creditCard = MutableLiveData<CreditCard>()
    val creditCard: LiveData<CreditCard>
        get() = _creditCard

    private var _bankAccounts = MutableLiveData<List<BankAccount>>()
    val bankAccounts: LiveData<List<BankAccount>>
        get() = _bankAccounts

    fun deleteCreditCard(cardId: Long) {
        viewModelScope.launch {
            creditCardRepository.deleteCreditCard(cardId)
        }
    }

    fun getAllCreditCardsWithTransactions(cardId: Long) {
        viewModelScope.launch {
            creditCardRepository.getAllCreditCardsWithTransactions(cardId).collect {
                _creditCard.postValue(it.creditCard.asModel())
            }
        }
    }

    fun getAllBankAccounts() {
        viewModelScope.launch {
            bankAccountRepository.getAllBankAccounts()
                .map { list ->
                    list.map {
                        it.asModel()
                    }
                }
                .flowOn(Dispatchers.Default)
                .collect {
                    _bankAccounts.postValue(it)
                }
        }
    }

    fun insertCardPurchase(cardPurchase: CardPurchase, creditCard: CreditCard) {
        viewModelScope.launch {
            cardPurchaseRepository.insertCardPurchase(cardPurchase.asEntity())
            creditCardRepository.updateCreditCard(creditCard.asEntity())
        }
    }

    fun insertInvoicePayment(
        invoicePayment: InvoicePayment,
        creditCard: CreditCard,
        bankAccount: BankAccount
    ) {
        viewModelScope.launch {
            invoicePaymentRepository.insertInvoicePayment(invoicePayment.asEntity())
            creditCardRepository.updateCreditCard(creditCard.asEntity())
            bankAccountRepository.updateBankAccount(bankAccount.asEntity())
        }
    }

}