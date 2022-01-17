package me.arturbruno.confinance.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.arturbruno.confinance.ConfinanceApplication
import me.arturbruno.confinance.R
import me.arturbruno.confinance.database.entities.asEntity
import me.arturbruno.confinance.models.*
import me.arturbruno.confinance.repositories.BankAccountRepository
import me.arturbruno.confinance.repositories.BankTransactionRepository
import javax.inject.Inject

@HiltViewModel
class BankAccountDetailsViewModel @Inject constructor(
    application: Application,
    private val bankAccountRepository: BankAccountRepository,
    private val bankTransactionRepository: BankTransactionRepository
) : AndroidViewModel(application) {

    private var _bankAccount = MutableLiveData<BankAccount>()
    val bankAccount: LiveData<BankAccount>
        get() = _bankAccount

    fun deleteBankAccount(bankId: Long) {
        viewModelScope.launch {
            bankAccountRepository.deleteBankAccount(bankId)
        }
    }

    fun getBankAccountWithTransactions(bankId: Long) {
        viewModelScope.launch {
            bankAccountRepository.getBankAccountWithTransactions(bankId)
                .collect {
                    _bankAccount.postValue(it.bankAccount.asModel())
                }
        }
    }

    fun insertBankTransaction(bankTransaction: BankTransaction, bankAccount: BankAccount) {
        viewModelScope.launch {
            bankTransactionRepository.insertBankTransaction(bankTransaction.asEntity())
            bankAccountRepository.updateBankAccount(bankAccount.asEntity())
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
