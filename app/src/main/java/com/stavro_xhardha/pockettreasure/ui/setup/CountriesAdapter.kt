package com.stavro_xhardha.pockettreasure.ui.setup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.model.Country
import kotlinx.android.synthetic.main.single_item_country.view.*


class CountriesAdapter(var countriesList: ArrayList<Country>, var setupContract: SetupContract) :
    RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder =
        CountriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_country, parent, false))


    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(countriesList[position], setupContract)
    }

    override fun getItemCount(): Int = countriesList.size


    class CountriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            country: Country,
            setupContract: SetupContract
        ) = with(itemView) {
            itemView.tvCountryName.text = country.name
            itemView.tvCapital.text = country.capitalCity
            llCountryHolder.setOnClickListener { setupContract.onListItemClick(country) }
        }
    }
}