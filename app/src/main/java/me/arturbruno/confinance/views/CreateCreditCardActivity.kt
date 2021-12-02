package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.*
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityCreateCreditCardBinding

class CreateCreditCardActivity : AppCompatActivity() {

    private val binding: ActivityCreateCreditCardBinding by lazy {
        ActivityCreateCreditCardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.dueDateInputEditText.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder
                .datePicker()
                .build()
            datePicker.addOnPositiveButtonClickListener { dateInLong ->
                val date =
                    Instant.fromEpochMilliseconds(dateInLong).toLocalDateTime(TimeZone.UTC)
                binding.dueDateInputEditText.setText(
                    getString(
                        R.string.date,
                        date.dayOfMonth,
                        date.monthNumber,
                        date.year
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