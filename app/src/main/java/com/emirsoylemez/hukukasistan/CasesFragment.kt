package com.emirsoylemez.hukukasistan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirsoylemez.hukukasistan.databinding.FragmentCasesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class CasesFragment : Fragment() {

    private var _binding: FragmentCasesBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var snapshotListener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCasesBinding.inflate(inflater, container, false)
        
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        (activity as MainActivity).setToolbarVisibility(View.VISIBLE)
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.casesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Firebase'den verileri getir
        fetchCasesFromFirestore()

        // Yeni dava ekleme butonu
        binding.addCaseButton.setOnClickListener {
            val action = CasesFragmentDirections.actionCasesFragmentToAddCaseFragment()
            findNavController().navigate(action)
        }
    }

    private fun fetchCasesFromFirestore() {
        val currentUser = auth.currentUser
        if (currentUser == null) return

        // Eski dinleyici varsa temizle
        snapshotListener?.remove()

        snapshotListener = db.collection("Cases")
            .whereEqualTo("userId", currentUser.uid)
            .addSnapshotListener { value, error ->
                // KRİTİK KONTROL: Eğer sayfa kapandıysa (binding null ise) işlem yapma
                if (_binding == null) return@addSnapshotListener
                
                if (error != null) {
                    Toast.makeText(requireContext(), "Veri alınamadı: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (value != null) {
                    val caseList = value.toObjects(CaseItem::class.java)
                    binding.casesRecyclerView.adapter = CasesAdapter(caseList)
                }
            }
    }

    override fun onDestroyView() {
        // Sayfa kapandığında dinleyiciyi durdur
        snapshotListener?.remove()
        super.onDestroyView()
        _binding = null
    }
}