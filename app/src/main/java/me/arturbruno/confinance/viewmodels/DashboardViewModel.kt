package me.arturbruno.confinance.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.arturbruno.confinance.models.*
import me.arturbruno.confinance.repositories.*
import me.arturbruno.confinance.views.Transaction
import me.arturbruno.confinance.views.dashboard.WalletData
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bankAccountRepository: BankAccountRepository,
    private val creditCardRepository: CreditCardRepository,
    private val invoicePaymentRepository: InvoicePaymentRepository,
    private val cardPurchaseRepository: CardPurchaseRepository,
    private val bankTransactionRepository: BankTransactionRepository
) : ViewModel() {

    private var _bankAccounts = MutableLiveData<List<BankAccount>>()
    val bankAccounts: LiveData<List<BankAccount>>
        get() = _bankAccounts

    private var _creditCards = MutableLiveData<List<CreditCard>>()
    val creditCards: LiveData<List<CreditCard>>
        get() = _creditCards

    private var _bankTransactions = MutableLiveData<List<BankTransaction>>()
    val bankTransactions: LiveData<List<BankTransaction>>
        get() = _bankTransactions

    private var _cardPurchases = MutableLiveData<List<CardPurchase>>()
    val cardPurchases: LiveData<List<CardPurchase>>
        get() = _cardPurchases

    private var _invoicePayments = MutableLiveData<List<InvoicePayment>>()
    val invoicePayments: LiveData<List<InvoicePayment>>
        get() = _invoicePayments

    private var _mixedData = MutableLiveData<List<WalletData>>()
    val mixedData: LiveData<List<WalletData>>
        get() = _mixedData

    private var _mixedTransactions = MutableLiveData<List<Transaction>>()
    val mixedTransactions: LiveData<List<Transaction>>
        get() = _mixedTransactions
    
    private var _balance = MutableLiveData<Double>()
    val balance: LiveData<Double>
        get() = _balance

    private var _incomes = MutableLiveData<Double>()
    val incomes: LiveData<Double>
        get() = _incomes

    private var _expenses = MutableLiveData<Double>()
    val expenses: LiveData<Double>
        get() = _expenses
    
    fun getAllBankAccounts() {
        viewModelScope.launch {
            bankAccountRepository.getAllBankAccounts().map { list ->
                list.map { it.asModel() }
            }
                .flowOn(Dispatchers.Default)
                .collect {
                    _bankAccounts.postValue(it)
                }
        }
    }

    fun getAllCreditCards() {
        viewModelScope.launch {
            creditCardRepository.getAllCreditCards().map { list ->
                list.map { it.asModel() }
            }
                .flowOn(Dispatchers.Default)
                .collect {
                    _creditCards.postValue(it)
                }
        }
    }

    fun getAllBankTransactions() {
        viewModelScope.launch {
            bankTransactionRepository.getAllBankTransactions().map { list ->
                list.map { it.asModel() }
            }
                .flowOn(Dispatchers.Default)
                .collect {
                    _bankTransactions.postValue(it)
                }
        }
    }

    fun getAllCardPurchases() {
        viewModelScope.launch {
            cardPurchaseRepository.getAllCardPurchases().map { list ->
                list.map { it.asModel() }
            }
                .flowOn(Dispatchers.Default)
                .collect {
                    _cardPurchases.postValue(it)
                }
        }
    }

    fun getAllInvoicePayments() {
        viewModelScope.launch {
            invoicePaymentRepository.getAllInvoicePayments().map { list ->
                list.map { it.asModel() }
            }
                .flowOn(Dispatchers.Default)
                .collect {
                    _invoicePayments.postValue(it)
                }
        }
    }

    fun mixData() {
        val mixedData = mutableListOf<WalletData>()
        viewModelScope.launch(Dispatchers.Default) {
            _bankAccounts.value?.forEach {
                mixedData.add(WalletData.BankAccountItem(it))
            }
            _creditCards.value?.forEach {
                mixedData.add(WalletData.CreditCardItem(it))
            }
        }.invokeOnCompletion {
            _mixedData.postValue(mixedData)
        }
    }

    fun mixTransactions() {
        val mixedData = mutableListOf<Transaction>()
        viewModelScope.launch(Dispatchers.Default) {
            _bankTransactions.value?.forEach {
                mixedData.add(Transaction.BankTransactionItem(it))
            }
            _cardPurchases.value?.forEach {
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

    fun getTotalBalance() {
        val bankAccounts = _bankAccounts.value
        val creditCards = _creditCards.value
        var totalBalance = 0.0
        viewModelScope.launch(Dispatchers.Default) {
            bankAccounts?.forEach {
                totalBalance += it.balance
            }
            creditCards?.forEach {
                totalBalance -= it.invoice
            }
        }.invokeOnCompletion {
            _balance.postValue(totalBalance)
        }
    }

    fun getTotalIncomes() {
        val bankAccounts = _bankAccounts.value
        var totalIncomes = 0.0
        viewModelScope.launch(Dispatchers.Default) {
            bankAccounts?.forEach {
                totalIncomes += it.balance
            }
        }.invokeOnCompletion {
            _incomes.postValue(totalIncomes)
        }
    }

    fun getTotalExpenses() {
        val creditCards = _creditCards.value
        var totalExpenses = 0.0
        viewModelScope.launch(Dispatchers.Default) {
            creditCards?.forEach {
                totalExpenses += it.invoice
            }
        }.invokeOnCompletion {
            _expenses.postValue(totalExpenses)
        }
    }
}