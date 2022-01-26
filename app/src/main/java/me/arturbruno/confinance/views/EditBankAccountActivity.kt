package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityEditBankAccountBinding
import me.arturbruno.confinance.models.BankAccount
import me.arturbruno.confinance.viewmodels.EditBankAccountViewModel

@AndroidEntryPoint
class EditBankAccountActivity : AppCompatActivity() {

    private val binding: ActivityEditBankAccountBinding by lazy {
        ActivityEditBankAccountBinding.inflate(layoutInflater)
    }

    private val viewModel: EditBankAccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val oldBankAccount = intent.getParcelableExtra<BankAccount>("bank_account")!!

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.apply {
            inputNameEditText.setText(oldBankAccount.name)
            inputBankTypeEditText.setText(viewModel.convertEnumOnText(oldBankAccount.type))
            inputBankEditText.setText(oldBankAccount.bank)
        }

        binding.saveAccount.setOnClickListener {
            viewModel.updateBankAccount(
                oldBankAccount.id,
                binding.inputNameEditText.text.toString(),
                binding.inputBankTypeEditText.text.toString(),
                binding.inputBankEditText.text.toString(),
                oldBankAccount.balance
            )
            finish()
        }

        val items = listOf(getString(R.string.checking_account), getString(R.string.savings_account))
        val adapter = ArrayAdapter(this, R.layout.list_text_field, items)
        (binding.inputBankType.editText as AutoCompleteTextView).setAdapter(adapter)

    }
}