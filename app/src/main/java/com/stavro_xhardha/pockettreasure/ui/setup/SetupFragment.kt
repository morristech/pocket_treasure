package com.stavro_xhardha.pockettreasure.ui.setup


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.MainActivity
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.model.Country
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject


class SetupFragment : BaseFragment(), SetupContract {
    @Inject
    lateinit var setupViewModelFactory: SetupViewModelFactory

    private lateinit var setupViewModel: SetupViewModel
    private lateinit var countriesAdapter: CountriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    override fun onStart() {
        super.onStart()
        (activity!! as MainActivity).supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        setupViewModel.loadListOfCountries()
    }

    override fun onStop() {
        super.onStop()
        (activity!! as MainActivity).supportActionBar?.show()
    }

    override fun performDi() {
        DaggerSetupComponent.builder()
            .pocketTreasureComponent(PocketTreasureApplication.getPocketTreasureComponent())
            .build().inject(this)
    }

    override fun initViewModel() {
        setupViewModel = ViewModelProviders.of(this, setupViewModelFactory).get(SetupViewModel::class.java)
    }

    override fun onListItemClick(country: Country) {
        setupViewModel.onCountrySelected(country)
        findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
    }

    override fun initializeComponents() {
        rvCountries.layoutManager = LinearLayoutManager(activity)
    }

    override fun observeTheLiveData() {
        setupViewModel.countriesList.observe(this, Observer {
            countriesAdapter = CountriesAdapter(it, this)
            rvCountries.adapter = countriesAdapter
        })
        setupViewModel.isCountryAndCapitalEmpty.observe(this, Observer {
            if (!it)
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
        })
        setupViewModel.errorVisibility.observe(this, Observer {
            llError.visibility = it
        })

        setupViewModel.contentVisibility.observe(this, Observer {
            rvCountries.visibility = it
        })

        setupViewModel.pbVisibility.observe(this, Observer {
            pbSetup.visibility = it
        })
    }

    override fun handleOnBackPressed(view: View) {
    }

}
