package com.emirsoylemez.hukukasistan

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.emirsoylemez.hukukasistan.databinding.FragmentAddCaseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddCaseFragment : Fragment() {

    private var _binding: FragmentAddCaseBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Seçilen PDF'leri listede tutuyoruz
    private val selectedPdfs = mutableListOf<PdfFile>()

    private val getPdfLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val localPath = savePdfLocally(it)
            if (localPath != null) {
                // Her seçimde listeye ekle
                val fileName = "Belge_${selectedPdfs.size + 1}.pdf"
                selectedPdfs.add(PdfFile(localPath, fileName))
                
                binding.selectedPdfNameText.text = "${selectedPdfs.size} Dosya Seçildi"
                Toast.makeText(requireContext(), "Dosya eklendi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCaseBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        (activity as? MainActivity)?.setBottomNavigationVisibility(View.GONE)
        (activity as? MainActivity)?.setToolbarVisibility(View.VISIBLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateEditText.setOnClickListener { showDatePicker() }
        binding.selectPdfButton.setOnClickListener { getPdfLauncher.launch("application/pdf") }
        binding.saveCaseButton.setOnClickListener { checkAndSave() }
        binding.cancelButton.setOnClickListener { findNavController().popBackStack() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val date = "$day/${month + 1}/$year"
                binding.dateEditText.setText(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun checkAndSave() {
        val title = binding.titleEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val date = binding.dateEditText.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = auth.currentUser ?: return
        binding.saveCaseButton.isEnabled = false

        saveCaseToFirestore(title, description, date, selectedPdfs)
    }

    private fun savePdfLocally(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val fileName = "case_pdf_${System.currentTimeMillis()}.pdf"
            val file = File(requireContext().filesDir, fileName)
            val outputStream = FileOutputStream(file)
            
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveCaseToFirestore(title: String, description: String, date: String, pdfFiles: List<PdfFile>) {
        val docRef = db.collection("Cases").document()

        val newCase = CaseItem(
            id = docRef.id,
            userId = auth.currentUser!!.uid,
            title = title,
            description = description,
            durusmaTarihi = date,
            durum = "Beklemede",
            pdfFiles = pdfFiles
        )

        docRef.set(newCase)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Dava başarıyla eklendi", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            .addOnFailureListener { e ->
                binding.saveCaseButton.isEnabled = true
                Toast.makeText(requireContext(), "Hata: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}