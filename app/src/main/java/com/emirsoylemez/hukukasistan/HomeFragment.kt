package com.emirsoylemez.hukukasistan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.emirsoylemez.hukukasistan.databinding.FragmentHomeBinding

private var _binding: FragmentHomeBinding? = null
private val binding get() = _binding!!

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        (activity as MainActivity).setToolbarVisibility(View.VISIBLE)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cases = listOf(
            CaseItem("Yılmaz vs. Kaya Davası", "Miras", "20.10.2026", "Beklemede"),
            CaseItem("Demir Ticaret Ltd.", "Ticari", "15.08.2024", "Sonuçlandı"),
            CaseItem("Arsa Sınırı", "Kadastro", "31.12.2025", "Beklemede")
        )

        val adapter = HomeCasesAdapter(cases)
        binding.casesViewPager.adapter = adapter

        binding.nextCaseButton.setOnClickListener {
            val current = binding.casesViewPager.currentItem
            if (current < adapter.itemCount - 1) {
                binding.casesViewPager.currentItem = current + 1
            }
        }

        binding.prevCaseButton.setOnClickListener {
            val current = binding.casesViewPager.currentItem
            if (current > 0) {
                binding.casesViewPager.currentItem = current - 1
            }
        }

        binding.scrimView.setOnClickListener {
        val action = HomeFragmentDirections.actionHomeFragmentToLawyerFragment()
            Navigation.findNavController(it).navigate(action)}

}}