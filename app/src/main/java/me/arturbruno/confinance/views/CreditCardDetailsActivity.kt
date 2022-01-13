package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.toLocalDateTime
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityCreditCardDetailsBinding
import me.arturbruno.confinance.getCurrencySymbol
import me.arturbruno.confinance.viewmodels.CreditCardDetailsViewModel

@AndroidEntryPoint
class CreditCardDetailsActivity : AppCompatActivity() {

    private val binding: ActivityCreditCardDetailsBinding by lazy {
        ActivityCreditCardDetailsBinding.inflate(layoutInflater)
    }

    private val viewModel: CreditCardDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.creditCard.observe(this) {
            binding.invoiceValue.text = getString(R.string.amount, getCurrencySymbol(), it.invoice)
            binding.nameAccount.text = it.name
            binding.nameInstitution.text = it.bank
            binding.limit.text = getString(R.string.amount, getCurrencySymbol(), it.limit)
            val date = it.invoiceDueDate.toLocalDateTime()
            binding.dueDate.text = getString(R.string.date, date.dayOfMonth, date.monthNumber, date.year)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllCreditCardsWithTransactions(intent.getLongExtra("id", -1))
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
                viewModel.deleteCreditCard(intent.getLongExtra("id", -1))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}