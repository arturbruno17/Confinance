package me.arturbruno.confinance.views.learning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import me.arturbruno.confinance.databinding.FragmentLearningScreen1Binding
import me.arturbruno.confinance.viewsmodels.LearningViewModel

class LearningScreen1Fragment : Fragment() {

    private val binding: FragmentLearningScreen1Binding by lazy {
        FragmentLearningScreen1Binding.inflate(layoutInflater)
    }

    private val viewModel: LearningViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.skip.setOnClickListener {
            // TODO Skip to DashBoardActivity
        }

        binding.goForward.setOnClickListener {
            viewModel.updatePage(1)
        }

        return binding.root
    }
}