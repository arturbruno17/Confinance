package me.arturbruno.confinance.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }

    private val rotateOpenFab: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_button) }
    private val rotateCloseFab: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_button) }
    private val showButton: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.show_button) }
    private val hideButton: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.hide_button) }

    private var active = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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