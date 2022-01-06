package me.arturbruno.confinance.views.learning

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.arturbruno.confinance.databinding.FragmentLearningScreen2Binding
import me.arturbruno.confinance.views.dashboard.DashboardActivity

class LearningScreen2Fragment : Fragment() {

    private val binding: FragmentLearningScreen2Binding by lazy {
        FragmentLearningScreen2Binding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.goForward.setOnClickListener {
            startActivity(Intent(requireContext(), DashboardActivity::class.java))
            requireActivity().finish()
        }

        return binding.root
    }

}