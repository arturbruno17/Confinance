package me.arturbruno.confinance.views.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityDashboardBinding
import me.arturbruno.confinance.getCurrencySymbol
import me.arturbruno.confinance.viewmodels.DashboardViewModel
import me.arturbruno.confinance.views.*

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    private lateinit var walletsAdapter: WalletsAdapter
    private lateinit var transactionsAdapter: TransactionsAdapter
    private val viewModel: DashboardViewModel by viewModels()

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

        setListeners()

        walletsAdapter = WalletsAdapter {
            val intent = when (it) {
                is WalletData.BankAccountItem -> {
                    Intent(applicationContext, BankAccountDetailsActivity::class.java).apply {
                        putExtra("id", it.data.id)
                    }
                }
                is WalletData.CreditCardItem -> {
                    Intent(applicationContext, CreditCardDetailsActivity::class.java).apply {
                        putExtra("id", it.data.id)
                    }
                }
            }
            startActivity(intent)
        }
        transactionsAdapter = TransactionsAdapter()

        binding.walletsList.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity, RecyclerView.HORIZONTAL, false)
            adapter = walletsAdapter
            addItemDecoration(WalletItemDecoration())
        }

        binding.transactionsList.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity, RecyclerView.VERTICAL, false)
            adapter = transactionsAdapter
        }

        setObservers()

        viewModel.getAllBankAccounts()
        viewModel.getAllCreditCards()
        viewModel.getAllBankTransactions()
        viewModel.getAllCardPurchases()
        viewModel.getAllInvoicePayments()
    }

    private fun setListeners() {
        binding.fabDashboard.setOnClickListener {
            active = if (!active) {
                it.startAnimation(rotateOpenFab)
                binding.fabCreateAccount.visibility = View.VISIBLE
                binding.fabCreateCard.visibility = View.VISIBLE
                binding.fabCreateAccount.startAnimation(showButton)
                binding.fabCreateCard.startAnimation(showButton)
                true
            } else {
                it.startAnimation(rotateCloseFab)
                binding.fabCreateAccount.visibility = View.GONE
                binding.fabCreateCard.visibility = View.GONE
                binding.fabCreateAccount.startAnimation(hideButton)
                binding.fabCreateCard.startAnimation(hideButton)
                false
            }
        }

        binding.fabCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateBankAccountActivity::class.java))
        }

        binding.fabCreateCard.setOnClickListener {
            startActivity(Intent(this, CreateCreditCardActivity::class.java))
        }
    }

    private fun setObservers() {
        viewModel.creditCards.observe(this) {
            viewModel.mixData()
        }

        viewModel.bankAccounts.observe(this) {
            viewModel.mixData()
        }

        viewModel.bankTransactions.observe(this) {
            viewModel.mixTransactions()
        }

        viewModel.cardPurchases.observe(this) {
            viewModel.mixTransactions()
        }

        viewModel.invoicePayments.observe(this) {
            viewModel.mixTransactions()
        }

        viewModel.mixedTransactions.observe(this) {
            transactionsAdapter.submitList(it)
        }

        viewModel.mixedData.observe(this) {
            walletsAdapter.submitList(it)

            viewModel.getTotalBalance()
            viewModel.getTotalIncomes()
            viewModel.getTotalExpenses()
        }

        viewModel.balance.observe(this) {
            binding.balanceValue.text = getString(R.string.amount, getCurrencySymbol(), it)
        }

        viewModel.incomes.observe(this) {
            binding.incomeValue.text = getString(R.string.amount, getCurrencySymbol(), it)
        }

        viewModel.expenses.observe(this) {
            binding.expenseValue.text = getString(R.string.amount, getCurrencySymbol(), it)
        }
    }
}