package com.emirsoylemez.hukukasistan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.Navigation
import com.emirsoylemez.hukukasistan.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

private var _binding: FragmentLoginBinding? = null
private val binding get() = _binding!!
private val auth = Firebase.auth


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

//        binding.LoginButton.setOnClickListener {
//            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
//            Navigation.findNavController(it).navigate(action)
//        }

//        binding.buttonGotoSign.setOnClickListener {
//            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
//            Navigation.findNavController(it).navigate(action)
//        }


        return binding.root
    }

    private fun gotoSignUp(view: View) {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        Navigation.findNavController(view).navigate(action)
    }


    private fun loginToApp(view: View) {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (email == "" || password == "") {
            Toast.makeText(requireContext(), "Enter email and password!", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                Navigation.findNavController(view).navigate(action)
            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Password is not true!", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            Navigation.findNavController(view).navigate(action)
        }
        binding.buttonGotoSign.setOnClickListener { gotoSignUp(view) }
        binding.LoginButton.setOnClickListener {
            loginToApp(view)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

}