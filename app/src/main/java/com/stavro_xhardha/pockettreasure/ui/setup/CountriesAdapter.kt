package com.stavro_xhardha.pockettreasure.ui.setup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.model.Country
import kotlinx.android.synthetic.main.single_item_country.view.*

class CountriesAdapter(var countriesList: ArrayList<Country>) :
    RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder =
        CountriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_country, parent, false))


    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(countriesList[position])
    }

    override fun getItemCount(): Int = countriesList.size


    class CountriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(country: Country) = with(itemView) {
            itemView.tvCountryName.text = country.name
            Picasso.get().load(country.flagUrl).fit().placeholder(R.drawable.ic_asr_sun).into(itemView.ivCountryFlag)
        }

    }
}