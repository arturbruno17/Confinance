package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityEditCreditCardBinding
import me.arturbruno.confinance.models.CreditCard
import me.arturbruno.confinance.viewmodels.EditCreditCardViewModel

@AndroidEntryPoint
class EditCreditCardActivity : AppCompatActivity() {

    private val binding: ActivityEditCreditCardBinding by lazy {
        ActivityEditCreditCardBinding.inflate(layoutInflater)
    }

    private val viewModel: EditCreditCardViewModel by viewModels()

    private lateinit var dateSelected: LocalDateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val oldCreditCard = intent.getParcelableExtra<CreditCard>("credit_card")!!
        dateSelected = oldCreditCard.invoiceDueDate.toLocalDateTime()

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

        binding.apply {
            inputNameEditText.setText(oldCreditCard.name)
            inputBankEditText.setText(oldCreditCard.bank)
            inputLimitEditText.setText(oldCreditCard.limit.toString())
            inputDueDateEditText.setText(getString(R.string.date, dateSelected.monthNumber, dateSelected.year))
        }

        binding.saveCard.setOnClickListener {
            viewModel.updateCreditCard(
                CreditCard(
                    oldCreditCard.id,
                    binding.inputNameEditText.text.toString(),
                    binding.inputLimitEditText.text.toString().toDoubleOrNull() ?: 0.0,
                    dateSelected.toString(),
                    binding.inputBankEditText.text.toString(),
                    oldCreditCard.invoice
                )
            )
            finish()
        }
    }
}
