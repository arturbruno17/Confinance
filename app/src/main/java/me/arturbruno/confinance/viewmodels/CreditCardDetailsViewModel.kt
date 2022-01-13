package me.arturbruno.confinance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.arturbruno.confinance.models.CreditCard
import me.arturbruno.confinance.models.asModel
import me.arturbruno.confinance.repositories.CreditCardRepository
import javax.inject.Inject

@HiltViewModel
class CreditCardDetailsViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository
) : ViewModel() {

    private var _creditCard = MutableLiveData<CreditCard>()
    val creditCard: LiveData<CreditCard>
        get() = _creditCard

    fun deleteCreditCard(cardId: Long) {
        viewModelScope.launch {
            creditCardRepository.deleteCreditCard(cardId)
        }
    }

    fun getAllCreditCardsWithTransactions(cardId: Long) {
        viewModelScope.launch {
            creditCardRepository.getAllCreditCardsWithTransactions(cardId).collect {
                _creditCard.postValue(it.creditCard.asModel())
            }
        }
    }
}