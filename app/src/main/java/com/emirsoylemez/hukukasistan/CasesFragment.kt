package com.emirsoylemez.hukukasistan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirsoylemez.hukukasistan.databinding.FragmentCasesBinding

private var _binding: FragmentCasesBinding? = null
private val binding get() = _binding!!


class CasesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCasesBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        (activity as MainActivity).setToolbarVisibility(View.VISIBLE)
        return binding.root
    }


    // onCreateView bittikten sonra view'lar hazır olduğunda bu metot çalışır.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Örnek veri listesi oluşturun (ileride bunu veritabanından veya API'den alacaksınız)
        val sampleCases = listOf(
            CaseItem("Yılmaz vs. Kaya Davası", "Miras paylaşımı anlaşmazlığı"),
            CaseItem("Demir Ticaret Ltd. Şti.", "Ticari alacak ve fatura itirazı"),
            CaseItem("Arsa Sınırı Tespiti", "Kadastro güncellemesi sonrası sınır ihlali"),
            CaseItem("Boşanma ve Velayet Davası", "Anlaşmalı boşanma protokolü hazırlığı"),
            CaseItem("İşe İade Talebi", "Haksız fesih nedeniyle açılan dava")
        )

        // 2. Adapter'ı oluşturun ve RecyclerView'a bağlayın
        val casesAdapter = CasesAdapter(sampleCases)
        binding.casesRecyclerView.adapter = casesAdapter // XML'deki RecyclerView'ınızın id'si "casesRecyclerView" olmalı

        // 3. RecyclerView'ın öğeleri nasıl dizeceğini belirtin (dikey bir liste için)
        binding.casesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    // Bellek sızıntılarını önlemek için view yok edildiğinde binding'i temizleyin
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}