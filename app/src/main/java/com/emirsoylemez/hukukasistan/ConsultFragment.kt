package com.emirsoylemez.hukukasistan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emirsoylemez.hukukasistan.databinding.FragmentConsultBinding

private var _binding: FragmentConsultBinding? = null
private val binding get() = _binding!!

class ConsultFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConsultBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        (activity as MainActivity).setToolbarVisibility(View.VISIBLE)
        return binding.root
    }


}