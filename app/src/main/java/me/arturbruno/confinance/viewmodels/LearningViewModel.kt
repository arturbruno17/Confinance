package me.arturbruno.confinance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LearningViewModel : ViewModel() {
    private val _actualPage = MutableLiveData(0)
    val actualPage: LiveData<Int>
        get() = _actualPage

    fun updatePage(page: Int) {
        _actualPage.value = page
    }
}