package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.arturbruno.confinance.databinding.ActivityCreditCardDetailsBinding

class CreditCardDetailsActivity : AppCompatActivity() {

    private val binding: ActivityCreditCardDetailsBinding by lazy {
        ActivityCreditCardDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}