package com.emirsoylemez.hukukasistan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emirsoylemez.hukukasistan.databinding.FragmentLoginBinding

private var _binding: FragmentLoginBinding? = null
private val binding get() = _binding!!

class LoginFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
        (activity as MainActivity).setToolbarVisibility(View.GONE)

        return binding.root
    }


}