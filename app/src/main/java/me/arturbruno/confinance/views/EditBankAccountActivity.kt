package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityEditBankAccountBinding

class EditBankAccountActivity : AppCompatActivity() {

    private val binding: ActivityEditBankAccountBinding by lazy {
        ActivityEditBankAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_bank_account)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val items = listOf(getString(R.string.checking_account), getString(R.string.savings_account))
        val adapter = ArrayAdapter(this, R.layout.list_text_field, items)
        (binding.inputBankType.editText as AutoCompleteTextView).setAdapter(adapter)

    }
}