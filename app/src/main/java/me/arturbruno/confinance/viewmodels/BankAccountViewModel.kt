package me.arturbruno.confinance.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.arturbruno.confinance.ConfinanceApplication
import me.arturbruno.confinance.R
import me.arturbruno.confinance.database.entities.asEntity
import me.arturbruno.confinance.models.AccountType
import me.arturbruno.confinance.models.BankAccount
import me.arturbruno.confinance.repositories.BankAccountRepository
import java.lang.IllegalArgumentException

class BankAccountViewModel(
    app: Application,
    private val repository: BankAccountRepository
) : AndroidViewModel(app) {
    fun insertBankAccount(name: String, type: String, bank: String, balance: Double) {
        val bankAccount = BankAccount(
            id = 0,
            name = name,
            type = convertTextOnEnum(type),
            bank = bank,
            balance = balance
        )
        viewModelScope.launch {
            repository.insertBankAccount(bankAccount.asEntity())
        }
    }

    private fun convertTextOnEnum(type: String): AccountType {
        val application = getApplication<ConfinanceApplication>()
        return if (type == application.getString(R.string.checking_account)) {
            AccountType.CHECKING_ACCOUNT
        } else {
            AccountType.SAVINGS_ACCOUNT
        }
    }

    class Factory(private val app: Application, private val repository: BankAccountRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BankAccountViewModel::class.java))
                return BankAccountViewModel(
                    app,
                    repository
                ) as T
            throw IllegalArgumentException("Unknown model")
        }
    }
}
