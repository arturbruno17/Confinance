package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityBankAccountDetailsBinding

class BankAccountDetailsActivity : AppCompatActivity() {

    private val binding: ActivityBankAccountDetailsBinding by lazy {
        ActivityBankAccountDetailsBinding.inflate(layoutInflater)
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