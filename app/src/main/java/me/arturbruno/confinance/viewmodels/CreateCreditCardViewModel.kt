package me.arturbruno.confinance.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.arturbruno.confinance.database.entities.asEntity
import me.arturbruno.confinance.models.CreditCard
import me.arturbruno.confinance.repositories.CreditCardRepository
import javax.inject.Inject

@HiltViewModel
class CreateCreditCardViewModel @Inject constructor(
    private val repository: CreditCardRepository
) : ViewModel() {
    fun insertCreditCard(creditCard: CreditCard) {
        viewModelScope.launch {
            repository.insertCreditCard(creditCard.asEntity())
        }
    }
}
