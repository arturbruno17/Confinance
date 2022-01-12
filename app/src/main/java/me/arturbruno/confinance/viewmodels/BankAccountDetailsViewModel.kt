package me.arturbruno.confinance.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.arturbruno.confinance.repositories.BankAccountRepository
import javax.inject.Inject

@HiltViewModel
class BankAccountDetailsViewModel @Inject constructor(
    private val bankAccountRepository: BankAccountRepository
) : ViewModel() {

    fun deleteBankAccount(bankId: Long) {
        viewModelScope.launch {
            bankAccountRepository.deleteBankAccount(bankId)
        }
    }

}
