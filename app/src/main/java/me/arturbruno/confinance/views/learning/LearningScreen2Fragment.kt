package me.arturbruno.confinance.views.learning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.arturbruno.confinance.databinding.FragmentLearningScreen2Binding

class LearningScreen2Fragment : Fragment() {

    private val binding: FragmentLearningScreen2Binding by lazy {
        FragmentLearningScreen2Binding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.goForward.setOnClickListener {
            // TODO Go to DashBoardActivity
        }

        return binding.root
    }

}