package com.emirsoylemez.hukukasistan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

// 1. Her bir liste öğesinin verisini tutacak olan data class
data class CaseItem(
    val title: String,
    val description: String,
    val durusmaTarihi: String, // Yeni alan
    val durum: String          // Yeni alan
)

// 2. RecyclerView için Adapter sınıfı
class CasesAdapter(private val caseList: List<CaseItem>) :
    RecyclerView.Adapter<CasesAdapter.CaseViewHolder>() {

    // 3. Her bir satırın view'larını (TextView vs.) tutan ViewHolder
    class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.caseTitleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.caseDescriptionTextView)

        // YENİ EKLENEN SATIRLAR:
        val dateTextView: TextView = itemView.findViewById(R.id.caseDateTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.caseStatusTextView)
    }

    // 4. Yeni bir ViewHolder (ve dolayısıyla yeni bir satır view'ı) oluşturur
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_case, parent, false)
        return CaseViewHolder(view)
    }

    // 5. Veriyi belirtilen pozisyondaki ViewHolder'a bağlar
    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        val currentItem = caseList[position]
        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.description
        holder.dateTextView.text = "Duruşma: ${currentItem.durusmaTarihi}" // Yeni veri bağlama
        holder.statusTextView.text = currentItem.durum.uppercase() // Yeni veri bağlama

        // Duruma göre arkaplan rengini dinamik olarak ayarla
        val context = holder.itemView.context
        if (currentItem.durum.equals("Beklemede", ignoreCase = true)) {
            holder.statusTextView.background =
                ContextCompat.getDrawable(context, R.drawable.status_background_beklemede)
        } else {
            holder.statusTextView.background =
                ContextCompat.getDrawable(context, R.drawable.status_background_sonuclandi)
        }
    }

    // 6. Listenin toplam eleman sayısını döndürür
    override fun getItemCount() = caseList.size
}
