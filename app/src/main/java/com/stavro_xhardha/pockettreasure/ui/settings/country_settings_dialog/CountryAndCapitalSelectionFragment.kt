package com.stavro_xhardha.pockettreasure.ui.settings.country_settings_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureViewModelFactory
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.ui.SharedViewModel
import com.stavro_xhardha.pockettreasure.ui.setup.CountryAdapter
import com.stavro_xhardha.pockettreasure.ui.setup.SetupContract
import kotlinx.android.synthetic.main.fragment_country_and_capital_selection.*
import javax.inject.Inject

class CountryAndCapitalSelectionFragment : DialogFragment(), SetupContract {
    @Inject
    lateinit var factory: PocketTreasureViewModelFactory
    private lateinit var countrySelectionViewModel: CountrySettingsViewModel
    private lateinit var adapter: CountryAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        return inflater.inflate(R.layout.fragment_country_and_capital_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        performDependencyInjection()
        initViewModel()
        observeLiveData()
    }

    private fun initViewModel() {
        countrySelectionViewModel =
            ViewModelProviders.of(this, factory).get(CountrySettingsViewModel::class.java)
    }

    private fun observeLiveData() {
        countrySelectionViewModel.countriesList.observe(this, Observer {
            adapter = CountryAdapter(it, this@CountryAndCapitalSelectionFragment)
            rvCountries.adapter = adapter
        })

        countrySelectionViewModel.countryAndCapital.observe(this, Observer {
            //sharedViewModel.updateCountry(it)
        })

        countrySelectionViewModel.fragmentReadyToClose.observe(this, Observer {
            dismiss()
        })
    }

    private fun performDependencyInjection() {
        PocketTreasureApplication
            .getPocketTreasureComponent()
            .inject(this)
    }

    override fun onListItemClick(country: Country) {
        countrySelectionViewModel.newCountrySelected(country)
    }
}