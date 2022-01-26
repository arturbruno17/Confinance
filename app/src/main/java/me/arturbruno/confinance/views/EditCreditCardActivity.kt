package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityEditCreditCardBinding
import me.arturbruno.confinance.models.CreditCard

class EditCreditCardActivity : AppCompatActivity() {

    private val binding: ActivityEditCreditCardBinding by lazy {
        ActivityEditCreditCardBinding.inflate(layoutInflater)
    }

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
    }
}
