package me.arturbruno.confinance.viewmodels

import android.util.Log
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
import kotlinx.coroutines.withContext
import me.arturbruno.confinance.database.entities.asEntity
import me.arturbruno.confinance.models.*
import me.arturbruno.confinance.repositories.*
import me.arturbruno.confinance.views.Transaction
import javax.inject.Inject

@HiltViewModel
class CreditCardDetailsViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
    private val cardPurchaseRepository: CardPurchaseRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val invoicePaymentRepository: InvoicePaymentRepository,
    private val bankTransactionRepository: BankTransactionRepository
) : ViewModel() {

    private var _creditCard = MutableLiveData<CreditCard>()
    val creditCard: LiveData<CreditCard>
        get() = _creditCard

    private var _bankAccounts = MutableLiveData<List<BankAccount>>()
    val bankAccounts: LiveData<List<BankAccount>>
        get() = _bankAccounts

    private var _cardPurchaseHistory = MutableLiveData<List<CardPurchase>>()
    val cardPurchaseHistory: LiveData<List<CardPurchase>>
        get() = _cardPurchaseHistory

    private var _invoicePayments = MutableLiveData<List<InvoicePayment>>()
    val invoicePayments: LiveData<List<InvoicePayment>>
        get() = _invoicePayments

    private var _mixedTransactions = MutableLiveData<List<Transaction>>()
    val mixedTransactions: LiveData<List<Transaction>>
        get() = _mixedTransactions

    fun deleteCreditCard(cardId: Long) {
        viewModelScope.launch {
            creditCardRepository.deleteCreditCard(cardId)
            cardPurchaseRepository.deleteCardPurchasesByCardId(cardId)
            invoicePaymentRepository.deleteInvoicePaymentsByCardId(cardId)
        }
    }

    fun getAllCreditCardsWithTransactions(cardId: Long) {
        viewModelScope.launch {
            try {
                creditCardRepository.getAllCreditCardsWithTransactions(cardId).collect {
                    _creditCard.postValue(it.creditCard.asModel())
                    setCardPurchaseHistory(it.cardPurchaseHistory)
                    setInvoicePayments(it.invoicePayment)
                }
            } catch (e: Exception) {
                Log.getStackTraceString(e)
            }
        }
    }

    private suspend fun setCardPurchaseHistory(list: List<me.arturbruno.confinance.database.entities.CardPurchase>?) {
        list?.let { list ->
            withContext(Dispatchers.Default) {
                val cardPurchaseHistory = list.map {
                    it.asModel()
                }
                _cardPurchaseHistory.postValue(cardPurchaseHistory)
            }
        }
    }

    private suspend fun setInvoicePayments(list: List<me.arturbruno.confinance.database.entities.InvoicePayment>?) {
        list?.let { list ->
            withContext(Dispatchers.Default) {
                val invoicePayments = list.map {
                    it.asModel()
                }
                _invoicePayments.postValue(invoicePayments)
            }
        }
    }

    fun mixTransactions() {
        val mixedData = mutableListOf<Transaction>()
        viewModelScope.launch(Dispatchers.Default) {
            _cardPurchaseHistory.value?.forEach {
                mixedData.add(Transaction.CardPurchaseItem(it))
            }
            _invoicePayments.value?.forEach {
                mixedData.add(Transaction.InvoicePaymentItem(it))
            }
        }.invokeOnCompletion {
            sortTransactions(mixedData)
        }
    }

    private fun sortTransactions(list: List<Transaction>) {
        var listOrdered = listOf<Transaction>()
        viewModelScope.launch(Dispatchers.Default) {
            listOrdered = list.sortedBy {
                it.date
            }.asReversed()
        }.invokeOnCompletion {
            _mixedTransactions.postValue(listOrdered)
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

    fun deleteTransaction(transaction: Transaction) {
        when (transaction) {
            is Transaction.CardPurchaseItem -> {
                viewModelScope.launch {
                    cardPurchaseRepository.deleteCardPurchase(transaction.data.asEntity())
                    creditCardRepository.updateCreditCardInvoiceById(transaction.data.cardId, -transaction.data.value)
                }
            }

            is Transaction.BankTransactionItem -> {
                viewModelScope.launch {
                    bankTransactionRepository.deleteBankTransaction(transaction.data.asEntity())
                }
            }

            is Transaction.InvoicePaymentItem -> {
                viewModelScope.launch {
                    invoicePaymentRepository.deleteInvoicePayment(transaction.data.asEntity())
                    bankAccountRepository.updateBankAccountBalanceById(transaction.data.accountId, transaction.data.value)
                    creditCardRepository.updateCreditCardInvoiceById(transaction.data.cardId, transaction.data.value)
                }
            }
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