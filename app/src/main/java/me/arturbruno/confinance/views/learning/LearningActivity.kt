package me.arturbruno.confinance.views.learning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import me.arturbruno.confinance.databinding.ActivityLearningBinding
import me.arturbruno.confinance.viewsmodels.LearningViewModel

class LearningActivity : AppCompatActivity() {

    private val binding: ActivityLearningBinding by lazy {
        ActivityLearningBinding.inflate(layoutInflater)
    }

    private val viewModel: LearningViewModel by viewModels()

    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val listFragment = listOf(LearningScreen1Fragment(), LearningScreen2Fragment())
        val adapter = LearningScreenAdapter(listFragment, this)

        viewPager2 = binding.viewPager
        viewPager2.adapter = adapter

        viewModel.actualPage.observe(this) {
            viewPager2.currentItem = it
        }
    }

    override fun onBackPressed() {
        if (viewPager2.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager2.currentItem--
        }
    }
}