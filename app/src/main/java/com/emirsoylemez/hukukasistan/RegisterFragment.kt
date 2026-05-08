package com.emirsoylemez.hukukasistan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.emirsoylemez.hukukasistan.databinding.FragmentRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun passwordSignUp() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        if (email == "" || password == "") {
            Toast.makeText(requireContext(), "Enter email and password", Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(requireContext(), "Sign up successful", Toast.LENGTH_LONG).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_registerFragment_to_loginFragment3)
            }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
        (activity as MainActivity).setToolbarVisibility(View.GONE)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.RegisterButton.setOnClickListener { passwordSignUp() }
    }

}