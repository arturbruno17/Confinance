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
import me.arturbruno.confinance.viewmodels.DashboardViewModel
import me.arturbruno.confinance.views.CreateBankAccountActivity
import me.arturbruno.confinance.views.CreateCreditCardActivity

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }

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

        val walletsAdapter = WalletsAdapter()
        binding.walletsList.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity, RecyclerView.HORIZONTAL, false)
            adapter = walletsAdapter
            addItemDecoration(WalletItemDecoration())
        }


        viewModel.creditCards.observe(this) {
            viewModel.mixData()
        }

        viewModel.bankAccounts.observe(this) {
            viewModel.mixData()
        }

        viewModel.mixedData.observe(this) {
            walletsAdapter.submitList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllBankAccounts()
        viewModel.getAllCreditCards()
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
}