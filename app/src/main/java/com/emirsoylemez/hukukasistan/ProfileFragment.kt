package com.emirsoylemez.hukukasistan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.emirsoylemez.hukukasistan.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        (activity as MainActivity).setToolbarVisibility(View.VISIBLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutButton.setOnClickListener { logOut() }

        auth.currentUser?.let {
            binding.profileEmail.text = it.email
        }
    }

    private fun logOut() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            val userId = currentUser.uid

            // 1. Cihazdaki fiziksel PDF dosyalarını temizle
            val folder = requireContext().filesDir
            folder.listFiles { file -> file.extension == "pdf" }?.forEach { it.delete() }

            // 2. Firestore'daki PDF listesini temizle (Yeni Çoklu Yapıya Uygun)
            db.collection("Cases")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val batch = db.batch()
                    for (document in documents) {
                        val docRef = db.collection("Cases").document(document.id)
                        // pdfFiles listesini boşaltıyoruz
                        batch.update(docRef, "pdfFiles", emptyList<PdfFile>())
                    }

                    batch.commit().addOnCompleteListener {
                        auth.signOut()
                        if (isAdded) {
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_profileFragment_to_loginFragment)
                        }
                    }
                }
                .addOnFailureListener {
                    auth.signOut()
                    if (isAdded) {
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_profileFragment_to_loginFragment)
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}