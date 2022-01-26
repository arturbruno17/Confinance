package me.arturbruno.confinance.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.arturbruno.confinance.ConfinanceApplication
import me.arturbruno.confinance.R
import me.arturbruno.confinance.database.entities.asEntity
import me.arturbruno.confinance.models.AccountType
import me.arturbruno.confinance.models.BankAccount
import me.arturbruno.confinance.repositories.BankAccountRepository
import javax.inject.Inject

@HiltViewModel
class EditBankAccountViewModel @Inject constructor(
    app: Application,
    private val bankAccountRepository: BankAccountRepository
) : AndroidViewModel(app) {

    fun updateBankAccount(id: Long, name: String, type: String, bank: String, balance: Double) {
        val bankAccount = BankAccount(
            id = id,
            name = name,
            type = convertTextOnEnum(type),
            bank = bank,
            balance = balance
        )
        viewModelScope.launch {
            bankAccountRepository.updateBankAccount(bankAccount.asEntity())
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

    fun convertEnumOnText(type: AccountType): String {
        val application = getApplication<ConfinanceApplication>()
        return if (type == AccountType.CHECKING_ACCOUNT) {
            application.getString(R.string.checking_account)
        } else {
            application.getString(R.string.savings_account)
        }
    }

}
