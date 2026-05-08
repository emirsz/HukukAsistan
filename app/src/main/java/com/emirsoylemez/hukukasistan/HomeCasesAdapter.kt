//package com.emirsoylemez.hukukasistan

package com.emirsoylemez.hukukasistan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emirsoylemez.hukukasistan.util.calculateRemainingDays
import androidx.core.content.ContextCompat

class HomeCasesAdapter(
    private val caseList: List<CaseItem>
) : RecyclerView.Adapter<HomeCasesAdapter.HomeCaseViewHolder>() {

    class HomeCaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.homeCaseTitle)
        val status: TextView = view.findViewById(R.id.homeCaseStatus)
        val remaining: TextView = view.findViewById(R.id.homeRemainingDays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_case, parent, false)
        return HomeCaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeCaseViewHolder, position: Int) {
        val item = caseList[position]
        val context = holder.itemView.context

        holder.title.text = item.title
        holder.status.text = item.durum.uppercase()

        val remainingDays = calculateRemainingDays(item.durusmaTarihi)

        // Kalan gün bilgisinin güncellenmesi
        holder.remaining.text = when {
            item.durum.equals("Sonuçlandı", true) -> "Dava sonuçlandı"
            remainingDays == -1L -> "Tarih Belirsiz" // YENİ EKLENEN DURUM
            remainingDays < 0 -> "Duruşma geçti"
            else -> "$remainingDays gün kaldı"
        }

        // Duruma göre arkaplan rengi ayarı
        holder.status.background = if (item.durum.equals("Beklemede", true)) {
            ContextCompat.getDrawable(context, R.drawable.status_background_beklemede)
        } else {
            ContextCompat.getDrawable(context, R.drawable.status_background_sonuclandi)
        }

        // Metin rengi ayarı
        if (item.durum.equals("Sonuçlandı", true) || remainingDays == -1L) {
            holder.remaining.setTextColor(
                ContextCompat.getColor(context, android.R.color.darker_gray)
            )
        } else {
            holder.remaining.setTextColor(
                ContextCompat.getColor(context, R.color.white)
            )
        }
    }

    override fun getItemCount(): Int = caseList.size
}
// region
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.emirsoylemez.hukukasistan.util.calculateRemainingDays
//import androidx.core.content.ContextCompat
//
//class HomeCasesAdapter(
//    private val caseList: List<CaseItem>
//) : RecyclerView.Adapter<HomeCasesAdapter.HomeCaseViewHolder>() {
//
//    class HomeCaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val title: TextView = view.findViewById(R.id.homeCaseTitle)
//        val status: TextView = view.findViewById(R.id.homeCaseStatus)
//        val remaining: TextView = view.findViewById(R.id.homeRemainingDays)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCaseViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_home_case, parent, false)
//        return HomeCaseViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: HomeCaseViewHolder, position: Int) {
//        val item = caseList[position]
//
//        holder.title.text = item.title
//        holder.status.text = item.durum.uppercase()
//
//        val remainingDays = calculateRemainingDays(item.durusmaTarihi)
//
//        holder.remaining.text = when {
//            item.durum.equals("Sonuçlandı", true) ->
//                "Dava sonuçlandı"
//
//            remainingDays < 0 ->
//                "Duruşma geçti"
//
//            else ->
//                "$remainingDays gün kaldı"
//        }
//        val context = holder.itemView.context
//        holder.status.background =
//            if (item.durum.equals("Beklemede", true)) {
//                ContextCompat.getDrawable(context, R.drawable.status_background_beklemede)
//            } else {
//                ContextCompat.getDrawable(context, R.drawable.status_background_sonuclandi)
//            }
//        // Sonuçlandıysa kalan gün yazısını gri yap
//        if (item.durum.equals("Sonuçlandı", true)) {
//            holder.remaining.setTextColor(
//                ContextCompat.getColor(context, android.R.color.darker_gray)
//            )
//        } else {
//            holder.remaining.setTextColor(
//                ContextCompat.getColor(context, R.color.white) // ya da colorPrimary
//            )
//        }
//
//    }
//
//    override fun getItemCount(): Int = caseList.size
//}
// endregion
