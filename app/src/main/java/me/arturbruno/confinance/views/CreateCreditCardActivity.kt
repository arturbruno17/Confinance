package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.*
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityCreateCreditCardBinding
import me.arturbruno.confinance.models.CreditCard
import me.arturbruno.confinance.viewmodels.CreateCreditCardViewModel

@AndroidEntryPoint
class CreateCreditCardActivity : AppCompatActivity() {

    private val binding: ActivityCreateCreditCardBinding by lazy {
        ActivityCreateCreditCardBinding.inflate(layoutInflater)
    }

    private val viewModel: CreateCreditCardViewModel by viewModels()

    private lateinit var dateSelected: LocalDateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.inputDueDateEditText.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder
                .datePicker()
                .build()
            datePicker.addOnPositiveButtonClickListener { dateInLong ->
                dateSelected =
                    Instant.fromEpochMilliseconds(dateInLong).toLocalDateTime(TimeZone.UTC)
                binding.inputDueDateEditText.setText(
                    getString(
                        R.string.date,
                        dateSelected.dayOfMonth,
                        dateSelected.monthNumber,
                        dateSelected.year
                    )
                )
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.createCard.setOnClickListener {
            val creditCard = CreditCard(
                id = 0,
                name = binding.inputNameEditText.text.toString(),
                limit = binding.inputLimitEditText.text.toString().toDouble(),
                invoiceDueDate = dateSelected.toString(),
                bank = binding.inputBankEditText.text.toString(),
                invoice = 0.0
            )
            viewModel.insertCreditCard(creditCard)
            finish()
        }
    }
}