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
import me.arturbruno.confinance.databinding.ActivityBankAccountDetailsBinding
import me.arturbruno.confinance.databinding.InputTransactionDialogBinding
import me.arturbruno.confinance.getCurrencySymbol
import me.arturbruno.confinance.models.BankTransaction
import me.arturbruno.confinance.viewmodels.BankAccountDetailsViewModel

@AndroidEntryPoint
class BankAccountDetailsActivity : AppCompatActivity() {

    private val binding: ActivityBankAccountDetailsBinding by lazy {
        ActivityBankAccountDetailsBinding.inflate(layoutInflater)
    }

    private val inputDialogBinding: InputTransactionDialogBinding by lazy {
        InputTransactionDialogBinding.inflate(layoutInflater)
    }

    private val viewModel: BankAccountDetailsViewModel by viewModels()

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
                val transaction =
                    viewModel.mixedTransactions.value?.get(viewHolder.absoluteAdapterPosition) ?: return
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        touchHelper.attachToRecyclerView(binding.transactionsList)

        val transactionsAdapter = TransactionsAdapter()

        binding.transactionsList.apply {
            layoutManager =
                LinearLayoutManager(this@BankAccountDetailsActivity, RecyclerView.VERTICAL, false)
            adapter = transactionsAdapter
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.fabTransaction.setOnClickListener {
            active = if (!active) {
                it.startAnimation(rotateOpenFab)
                binding.fabDeposit.visibility = View.VISIBLE
                binding.fabDrawOut.visibility = View.VISIBLE
                binding.fabDeposit.startAnimation(showButton)
                binding.fabDrawOut.startAnimation(showButton)
                true
            } else {
                it.startAnimation(rotateCloseFab)
                binding.fabDeposit.visibility = View.GONE
                binding.fabDrawOut.visibility = View.GONE
                binding.fabDeposit.startAnimation(hideButton)
                binding.fabDrawOut.startAnimation(hideButton)
                false
            }
        }

        binding.fabDeposit.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.deposit))
                .setView(inputDialogBinding.root)
                .setPositiveButton(R.string.to_deposit) { dialog, which ->
                    val name = inputDialogBinding.inputNameEditText.text.toString()
                    val value =
                        inputDialogBinding.inputValueEditText.text.toString().toDoubleOrNull()
                            ?: 0.0
                    viewModel.bankAccount.value?.let {
                        viewModel.insertBankTransaction(
                            BankTransaction(
                                id = 0,
                                name = name,
                                value = value,
                                date = Clock.System.now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                                    .toString(),
                                bankId = it.id
                            ),
                            it.copy(balance = it.balance + value)
                        )
                    }
                }.setNegativeButton(R.string.cancel, null)
                .setOnDismissListener {
                    val parent = inputDialogBinding.root.parent as ViewGroup
                    parent.removeView(inputDialogBinding.root)
                }
                .show()
        }

        binding.fabDrawOut.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.draw_out))
                .setView(inputDialogBinding.root)
                .setPositiveButton(R.string.to_draw_out) { dialog, which ->
                    val name = inputDialogBinding.inputNameEditText.text.toString()
                    val value =
                        -(inputDialogBinding.inputValueEditText.text.toString().toDoubleOrNull()
                            ?: 0.0)
                    viewModel.bankAccount.value?.let {
                        viewModel.insertBankTransaction(
                            BankTransaction(
                                id = 0,
                                name = name,
                                value = value,
                                date = Clock.System.now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                                    .toString(),
                                bankId = it.id
                            ),
                            it.copy(balance = it.balance + value)
                        )
                    }
                }.setNegativeButton(R.string.cancel, null)
                .setOnDismissListener {
                    val parent = inputDialogBinding.root.parent as ViewGroup
                    parent.removeView(inputDialogBinding.root)
                }
                .show()
        }

        viewModel.bankAccount.observe(this) {
            binding.balanceValue.text = getString(R.string.amount, getCurrencySymbol(), it.balance)
            binding.nameAccount.text = it.name
            binding.nameInstitution.text = it.bank
            binding.typeAccount.text = viewModel.convertEnumOnText(it.type)
        }

        viewModel.bankTransactions.observe(this) {
            viewModel.mixTransactions()
        }

        viewModel.invoicePayments.observe(this) {
            viewModel.mixTransactions()
        }

        viewModel.mixedTransactions.observe(this) {
            transactionsAdapter.submitList(it)
        }

        viewModel.incomes.observe(this) {
            binding.incomeValue.text = getString(R.string.amount, getCurrencySymbol(), it)
        }

        viewModel.expenses.observe(this) {
            binding.expenseValue.text = getString(R.string.amount, getCurrencySymbol(), it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getBankAccountWithTransactions(intent.getLongExtra("id", -1))
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