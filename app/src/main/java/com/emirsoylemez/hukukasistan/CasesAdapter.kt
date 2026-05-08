package com.emirsoylemez.hukukasistan

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

data class PdfFile(
    val path: String = "",
    val name: String = ""
)

data class CaseItem(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val durusmaTarihi: String = "",
    val durum: String = "",
    val pdfFiles: List<PdfFile> = emptyList()
)

class CasesAdapter(private val caseList: List<CaseItem>) :
    RecyclerView.Adapter<CasesAdapter.CaseViewHolder>() {

    class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.caseTitleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.caseDescriptionTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.caseDateTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.caseStatusTextView)
        val pdfIcon: ImageView = itemView.findViewById(R.id.pdfIcon)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteCaseButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_case, parent, false)
        return CaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        val currentItem = caseList[position]
        val context = holder.itemView.context
        
        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.description
        holder.dateTextView.text = "Duruşma: ${currentItem.durusmaTarihi}"
        holder.statusTextView.text = currentItem.durum.uppercase()

        // Durum Arkaplanı
        if (currentItem.durum.equals("Beklemede", ignoreCase = true)) {
            holder.statusTextView.background = ContextCompat.getDrawable(context, R.drawable.status_background_beklemede)
        } else {
            holder.statusTextView.background = ContextCompat.getDrawable(context, R.drawable.status_background_sonuclandi)
        }

        // --- ÇOKLU YEREL PDF KONTROLÜ ---
        val validFiles = currentItem.pdfFiles.filter { File(it.path).exists() }

        if (validFiles.isNotEmpty()) {
            holder.pdfIcon.visibility = View.VISIBLE
            holder.pdfIcon.setOnClickListener {
                if (validFiles.size == 1) {
                    openPdf(validFiles[0].path, context)
                } else {
                    val fileNames = validFiles.map { it.name }.toTypedArray()
                    AlertDialog.Builder(context)
                        .setTitle("Ekli Dosyalar")
                        .setItems(fileNames) { _, which ->
                            openPdf(validFiles[which].path, context)
                        }
                        .show()
                }
            }
        } else {
            holder.pdfIcon.visibility = View.GONE
        }

        // --- DAVA SİLME İŞLEMİ ---
        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Davayı Sil")
                .setMessage("\"${currentItem.title}\" davasını silmek istediğinize emin misiniz?")
                .setPositiveButton("Evet") { _, _ ->
                    deleteCase(currentItem, context)
                }
                .setNegativeButton("Hayır", null)
                .show()
        }
    }

    private fun openPdf(path: String, context: android.content.Context) {
        try {
            val file = File(path)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "PDF açılamadı", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteCase(item: CaseItem, context: android.content.Context) {
        val db = FirebaseFirestore.getInstance()
        
        db.collection("Cases").document(item.id)
            .delete()
            .addOnSuccessListener {
                // Tüm yerel dosyaları sil
                item.pdfFiles.forEach { pdf ->
                    val file = File(pdf.path)
                    if (file.exists()) {
                        file.delete()
                    }
                }
                Toast.makeText(context, "Dava başarıyla silindi", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount() = caseList.size
}