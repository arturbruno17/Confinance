package me.arturbruno.confinance.views

import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityCreditCardDetailsBinding
import me.arturbruno.confinance.databinding.InputInvoicePaymentDialogBinding
import me.arturbruno.confinance.databinding.InputTransactionDialogBinding
import me.arturbruno.confinance.getCurrencySymbol
import me.arturbruno.confinance.models.CardPurchase
import me.arturbruno.confinance.models.InvoicePayment
import me.arturbruno.confinance.viewmodels.CreditCardDetailsViewModel

@AndroidEntryPoint
class CreditCardDetailsActivity : AppCompatActivity() {

    private val binding: ActivityCreditCardDetailsBinding by lazy {
        ActivityCreditCardDetailsBinding.inflate(layoutInflater)
    }

    private val inputDialogBinding: InputTransactionDialogBinding by lazy {
        InputTransactionDialogBinding.inflate(layoutInflater)
    }

    private val inputInvoicePaymentDialogBinding: InputInvoicePaymentDialogBinding by lazy {
        InputInvoicePaymentDialogBinding.inflate(layoutInflater)
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

    private val touchHelper: ItemTouchHelper by lazy {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val transaction = viewModel.mixedTransactions.value?.get(viewHolder.absoluteAdapterPosition) ?: return
                viewModel.deleteTransaction(transaction)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(getColor(R.color.red))
                    .addActionIcon(R.drawable.ic_baseline_delete_outline_24)
                    .setActionIconTint(R.color.dark_gray)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        })
    }

    private var active = false
    private var positionItemClicked = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        touchHelper.attachToRecyclerView(binding.transactionsList)

        val transactionsAdapter = TransactionsAdapter()

        binding.transactionsList.apply {
            layoutManager = LinearLayoutManager(this@CreditCardDetailsActivity, RecyclerView.VERTICAL, false)
            adapter = transactionsAdapter
        }

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

        inputInvoicePaymentDialogBinding.inputBankAccountEditText.setOnItemClickListener { _, _, position, _ ->
            positionItemClicked = position
        }

        binding.fabInvoicePayment.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.pay_invoice))
                .setView(inputInvoicePaymentDialogBinding.root)
                .setPositiveButton(R.string.to_pay) { dialog, which ->
                    val value = inputInvoicePaymentDialogBinding.inputValueEditText.text.toString().toDoubleOrNull() ?: 0.0
                    viewModel.creditCard.value?.let { creditCard ->
                        val bankAccount = viewModel.bankAccounts.value?.get(positionItemClicked) ?: return@setPositiveButton
                        viewModel.insertInvoicePayment(
                            InvoicePayment(
                                id = 0,
                                value = value,
                                date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
                                cardId = creditCard.id,
                                accountId = bankAccount.id
                            ),
                            creditCard.copy(invoice = creditCard.invoice - value),
                            bankAccount.copy(balance = bankAccount.balance - value)
                        )
                    }
                }.setNegativeButton(R.string.cancel, null)
                .setOnDismissListener {
                    val parent = inputInvoicePaymentDialogBinding.root.parent as ViewGroup
                    parent.removeView(inputInvoicePaymentDialogBinding.root)
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

        viewModel.bankAccounts.observe(this) { list ->
            val items = mutableListOf<String>()
            list?.map {
                items.add(it.name)
            }
            val adapter = ArrayAdapter(this, R.layout.list_text_field, items)
            inputInvoicePaymentDialogBinding.inputBankAccountEditText.setAdapter(adapter)
        }

        viewModel.invoicePayments.observe(this) {
            viewModel.mixTransactions()
        }

        viewModel.cardPurchaseHistory.observe(this) {
            viewModel.mixTransactions()
        }

        viewModel.mixedTransactions.observe(this) {
            transactionsAdapter.submitList(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllCreditCardsWithTransactions(intent.getLongExtra("id", -1))
        viewModel.getAllBankAccounts()
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