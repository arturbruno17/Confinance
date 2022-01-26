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
class EditCreditCardViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository
): ViewModel() {

    fun updateCreditCard(creditCard: CreditCard) {
        viewModelScope.launch {
            creditCardRepository.updateCreditCard(creditCard.asEntity())
        }
    }

}
