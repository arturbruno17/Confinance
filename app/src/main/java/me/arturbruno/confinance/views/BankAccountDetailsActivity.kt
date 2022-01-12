package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityBankAccountDetailsBinding
import me.arturbruno.confinance.viewmodels.BankAccountDetailsViewModel

@AndroidEntryPoint
class BankAccountDetailsActivity : AppCompatActivity() {

    private val binding: ActivityBankAccountDetailsBinding by lazy {
        ActivityBankAccountDetailsBinding.inflate(layoutInflater)
    }

    private val viewModel: BankAccountDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.wallet_details_menu, it)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                viewModel.deleteBankAccount(intent.getLongExtra("id", -1))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}