package com.emirsoylemez.hukukasistan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 1. Her bir liste öğesinin verisini tutacak olan data class
data class CaseItem(val title: String, val description: String)

// 2. RecyclerView için Adapter sınıfı
class CasesAdapter(private val caseList: List<CaseItem>) : RecyclerView.Adapter<CasesAdapter.CaseViewHolder>() {

    // 3. Her bir satırın view'larını (TextView vs.) tutan ViewHolder
    class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.caseTitleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.caseDescriptionTextView)
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
    }

    // 6. Listenin toplam eleman sayısını döndürür
    override fun getItemCount() = caseList.size
}