package me.arturbruno.confinance.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityCreditCardDetailsBinding
import me.arturbruno.confinance.databinding.InputTransactionDialogBinding
import me.arturbruno.confinance.getCurrencySymbol
import me.arturbruno.confinance.models.CardPurchase
import me.arturbruno.confinance.viewmodels.CreditCardDetailsViewModel

@AndroidEntryPoint
class CreditCardDetailsActivity : AppCompatActivity() {

    private val binding: ActivityCreditCardDetailsBinding by lazy {
        ActivityCreditCardDetailsBinding.inflate(layoutInflater)
    }

    private val inputDialogBinding: InputTransactionDialogBinding by lazy {
        InputTransactionDialogBinding.inflate(layoutInflater)
    }

    private val viewModel: CreditCardDetailsViewModel by viewModels()

    private val rotateOpenFab: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_button
        )
    }
    private val rotateCloseFab: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_button
        )
    }
    private val showButton: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.show_button
        )
    }
    private val hideButton: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.hide_button
        )
    }

    private var active = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.fabTransaction.setOnClickListener {
            active = if (!active) {
                it.startAnimation(rotateOpenFab)
                binding.fabNewBuy.visibility = View.VISIBLE
                binding.fabInvoicePayment.visibility = View.VISIBLE
                binding.fabNewBuy.startAnimation(showButton)
                binding.fabInvoicePayment.startAnimation(showButton)
                true
            } else {
                it.startAnimation(rotateCloseFab)
                binding.fabNewBuy.visibility = View.GONE
                binding.fabInvoicePayment.visibility = View.GONE
                binding.fabNewBuy.startAnimation(hideButton)
                binding.fabInvoicePayment.startAnimation(hideButton)
                false
            }
        }


        binding.fabNewBuy.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.new_buy))
                .setView(inputDialogBinding.root)
                .setPositiveButton(R.string.to_buy) { dialog, which ->
                    val name = inputDialogBinding.inputNameEditText.text.toString()
                    val value =
                        inputDialogBinding.inputValueEditText.text.toString().toDoubleOrNull() ?: 0.0
                    viewModel.creditCard.value?.let {
                        viewModel.insertCardPurchase(
                            CardPurchase(
                                id = 0,
                                name = name,
                                value = value,
                                date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
                                cardId = it.id
                            ),
                            it.copy(invoice = it.invoice + value)
                        )
                    }
                }.setNegativeButton(R.string.cancel, null)
                .setOnDismissListener {
                    val parent = inputDialogBinding.root.parent as ViewGroup
                    parent.removeView(inputDialogBinding.root)
                }
                .show()
        }

        viewModel.creditCard.observe(this) {
            binding.invoiceValue.text = getString(R.string.amount, getCurrencySymbol(), it.invoice)
            binding.nameAccount.text = it.name
            binding.nameInstitution.text = it.bank
            binding.limit.text = getString(R.string.amount, getCurrencySymbol(), it.limit)
            val date = it.invoiceDueDate.toLocalDateTime()
            binding.dueDate.text = getString(R.string.date, date.monthNumber, date.year)
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