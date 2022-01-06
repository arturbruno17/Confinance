package me.arturbruno.confinance.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.arturbruno.confinance.models.BankAccount
import me.arturbruno.confinance.models.CreditCard
import me.arturbruno.confinance.models.asModel
import me.arturbruno.confinance.repositories.BankAccountRepository
import me.arturbruno.confinance.repositories.CreditCardRepository
import me.arturbruno.confinance.views.dashboard.WalletData
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bankAccountRepository: BankAccountRepository,
    private val creditCardRepository: CreditCardRepository
) : ViewModel() {

    private var _bankAccounts = MutableLiveData<List<BankAccount>>()
    val bankAccounts: LiveData<List<BankAccount>>
        get() = _bankAccounts

    private var _creditCards = MutableLiveData<List<CreditCard>>()
    val creditCards: LiveData<List<CreditCard>>
        get() = _creditCards

    private var _mixedData = MutableLiveData<List<WalletData>>()
    val mixedData: LiveData<List<WalletData>>
        get() = _mixedData

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
}