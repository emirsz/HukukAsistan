package com.emirsoylemez.hukukasistan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.emirsoylemez.hukukasistan.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var snapshotListener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        (activity as MainActivity).setToolbarVisibility(View.VISIBLE)

        binding.welcomeProfileCard.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            Navigation.findNavController(it).navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firestore'dan verileri çek ve ViewPager'a bağla
        fetchHomeCases()

        binding.scrimView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToLawyerFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun fetchHomeCases() {
        val currentUser = auth.currentUser ?: return

        snapshotListener?.remove()
        snapshotListener = db.collection("Cases")
            .whereEqualTo("userId", currentUser.uid)
            .addSnapshotListener { value, error ->
                // Çökmeyi önlemek için kritik kontrol: Fragment view'ı yok edildiyse dur.
                if (_binding == null) return@addSnapshotListener
                
                if (error != null || value == null) return@addSnapshotListener
                
                val caseList = value.toObjects(CaseItem::class.java)
                val adapter = HomeCasesAdapter(caseList)
                binding.casesViewPager.adapter = adapter

                // Buton kontrolleri
                binding.nextCaseButton.setOnClickListener {
                    if (_binding != null) {
                        val current = binding.casesViewPager.currentItem
                        if (current < adapter.itemCount - 1) {
                            binding.casesViewPager.currentItem = current + 1
                        }
                    }
                }

                binding.prevCaseButton.setOnClickListener {
                    if (_binding != null) {
                        val current = binding.casesViewPager.currentItem
                        if (current > 0) {
                            binding.casesViewPager.currentItem = current - 1
                        }
                    }
                }
                
                if (caseList.isEmpty()) {
                    binding.upcomingCasesCard.visibility = View.VISIBLE
                }
            }
    }

    override fun onDestroyView() {
        // Dinleyiciyi durdurmak sızıntıları ve çökmeleri önler
        snapshotListener?.remove()
        super.onDestroyView()
        _binding = null
    }
}