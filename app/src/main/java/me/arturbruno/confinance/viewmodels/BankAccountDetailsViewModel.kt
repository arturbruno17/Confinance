package me.arturbruno.confinance.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.arturbruno.confinance.ConfinanceApplication
import me.arturbruno.confinance.R
import me.arturbruno.confinance.database.entities.asEntity
import me.arturbruno.confinance.models.*
import me.arturbruno.confinance.repositories.*
import me.arturbruno.confinance.views.Transaction
import javax.inject.Inject

@HiltViewModel
class BankAccountDetailsViewModel @Inject constructor(
    application: Application,
    private val bankAccountRepository: BankAccountRepository,
    private val bankTransactionRepository: BankTransactionRepository,
    private val invoicePaymentRepository: InvoicePaymentRepository,
    private val cardPurchaseRepository: CardPurchaseRepository,
    private val creditCardRepository: CreditCardRepository
) : AndroidViewModel(application) {

    private var _bankAccount = MutableLiveData<BankAccount>()
    val bankAccount: LiveData<BankAccount>
        get() = _bankAccount

    private var _bankTransactions = MutableLiveData<List<BankTransaction>>()
    val bankTransactions: LiveData<List<BankTransaction>>
        get() = _bankTransactions

    private var _invoicePayments = MutableLiveData<List<InvoicePayment>>()
    val invoicePayments: LiveData<List<InvoicePayment>>
        get() = _invoicePayments

    private var _mixedTransactions = MutableLiveData<List<Transaction>>()
    val mixedTransactions: LiveData<List<Transaction>>
        get() = _mixedTransactions

    private var _incomes = MutableLiveData(0.0)
    val incomes: LiveData<Double>
        get() = _incomes

    private var _expenses = MutableLiveData(0.0)
    val expenses: LiveData<Double>
        get() = _expenses

    fun deleteBankAccount(bankId: Long) {
        viewModelScope.launch {
            bankAccountRepository.deleteBankAccount(bankId)
            bankTransactionRepository.deleteBankTransactionsByBankId(bankId)
        }
    }

    fun getBankAccountWithTransactions(bankId: Long) {
        viewModelScope.launch {
            try {
                bankAccountRepository.getBankAccountWithTransactions(bankId)
                    .collect {
                        _bankAccount.postValue(it.bankAccount.asModel())
                        setBankTransactionHistory(it.bankTransactionHistory)
                        setInvoicePayments(it.invoicePayment)
                    }
            } catch(e: Exception) {
                Log.getStackTraceString(e)
            }
        }
    }

    private suspend fun setBankTransactionHistory(list: List<me.arturbruno.confinance.database.entities.BankTransaction>?) {
        list?.let { list ->
            withContext(Dispatchers.Default) {
                var incomes = 0.0
                var expenses = 0.0
                val bankTransactions = list.map {
                    if (it.value > 0) {
                        incomes += it.value
                    } else if (it.value < 0) {
                        expenses += -it.value
                    }
                    it.asModel()
                }
                _incomes.postValue(incomes)
                _expenses.postValue(expenses)
                _bankTransactions.postValue(bankTransactions)
            }
        }
    }

    private suspend fun setInvoicePayments(list: List<me.arturbruno.confinance.database.entities.InvoicePayment>?) {
        list?.let { list ->
            withContext(Dispatchers.Default) {
                var expenses = expenses.value ?: 0.0
                val invoicePayments = list.map {
                    expenses += it.value
                    it.asModel()
                }
                _expenses.postValue(expenses)
                _invoicePayments.postValue(invoicePayments)
            }
        }
    }

    fun mixTransactions() {
        val mixedData = mutableListOf<Transaction>()
        viewModelScope.launch(Dispatchers.Default) {
            _bankTransactions.value?.forEach {
                mixedData.add(Transaction.BankTransactionItem(it))
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

    fun insertBankTransaction(bankTransaction: BankTransaction, bankAccount: BankAccount) {
        viewModelScope.launch {
            bankTransactionRepository.insertBankTransaction(bankTransaction.asEntity())
            bankAccountRepository.updateBankAccount(bankAccount.asEntity())
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        when (transaction) {
            is Transaction.CardPurchaseItem -> {
                viewModelScope.launch {
                    cardPurchaseRepository.deleteCardPurchase(transaction.data.asEntity())
                }
            }

            is Transaction.BankTransactionItem -> {
                viewModelScope.launch {
                    bankTransactionRepository.deleteBankTransaction(transaction.data.asEntity())
                    bankAccountRepository.updateBankAccountBalanceById(
                        transaction.data.bankId,
                        -transaction.data.value
                    )
                }
            }

            is Transaction.InvoicePaymentItem -> {
                viewModelScope.launch {
                    invoicePaymentRepository.deleteInvoicePayment(transaction.data.asEntity())
                    bankAccountRepository.updateBankAccountBalanceById(
                        transaction.data.accountId,
                        transaction.data.value
                    )
                    creditCardRepository.updateCreditCardInvoiceById(
                        transaction.data.cardId,
                        transaction.data.value
                    )
                }
            }
        }
    }

    fun convertEnumOnText(type: AccountType): String {
        val application = getApplication<ConfinanceApplication>()
        return if (type == AccountType.CHECKING_ACCOUNT) {
            application.getString(R.string.checking_account)
        } else {
            application.getString(R.string.savings_account)
        }
    }

}
