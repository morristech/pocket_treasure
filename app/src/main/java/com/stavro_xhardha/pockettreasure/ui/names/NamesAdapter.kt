package com.stavro_xhardha.pockettreasure.ui.names

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.model.Name
import kotlinx.android.synthetic.main.single_item_name.view.*

class NamesAdapter(var namesList: ArrayList<Name>) : RecyclerView.Adapter<NamesAdapter.NamesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamesAdapter.NamesViewHolder =
        NamesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_name, parent, false))

    override fun getItemCount(): Int = namesList.size

    override fun onBindViewHolder(holder: NamesAdapter.NamesViewHolder, position: Int) {
        holder.bind(namesList[position])
    }

    class NamesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(name: Name) = with(itemView) {
            itemView.tvArabicName.text = name.arabicName
            itemView.tvMeaning.text = name.englishNameMeaning.meaning
            itemView.tvTransliteration.text = name.transliteration
        }

    }
}