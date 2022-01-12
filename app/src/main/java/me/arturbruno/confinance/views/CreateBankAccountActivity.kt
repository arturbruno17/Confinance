package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityCreateBankAccountBinding
import me.arturbruno.confinance.viewmodels.CreateBankAccountViewModel

@AndroidEntryPoint
class CreateBankAccountActivity : AppCompatActivity() {

    private val binding: ActivityCreateBankAccountBinding by lazy {
        ActivityCreateBankAccountBinding.inflate(layoutInflater)
    }

    private val viewModel: CreateBankAccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val items = listOf(getString(R.string.checking_account), getString(R.string.savings_account))
        val adapter = ArrayAdapter(this, R.layout.list_text_field, items)
        (binding.inputBankType.editText as AutoCompleteTextView).setAdapter(adapter)

        binding.createAccount.setOnClickListener {
            viewModel.insertBankAccount(
                binding.inputNameEditText.text.toString(),
                binding.inputBankTypeEditText.text.toString(),
                binding.inputBankEditText.text.toString(),
                binding.inputBalanceEditText.text.toString().toDoubleOrNull() ?: 0.0
            )
            finish()
        }
    }
}