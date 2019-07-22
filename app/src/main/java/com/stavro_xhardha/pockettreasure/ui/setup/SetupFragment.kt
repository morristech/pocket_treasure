package com.stavro_xhardha.pockettreasure.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureViewModelFactory
import com.stavro_xhardha.pockettreasure.model.Country
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject


class SetupFragment : BaseFragment(), SetupContract {
    @Inject
    lateinit var factory: PocketTreasureViewModelFactory

    private lateinit var setupViewModel: SetupViewModel
    private lateinit var countryAdapter: CountryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    override fun performDi() {
        component.inject(this)
    }

    override fun initViewModel() {
        setupViewModel = ViewModelProviders.of(this, factory).get(SetupViewModel::class.java)
    }

    override fun onListItemClick(country: Country) {
        setupViewModel.onCountrySelected(country)
        setupViewModel.saveCountriesToDatabase()
        askForNotifyingUser()
    }

    private fun askForNotifyingUser() {
        MaterialDialog(activity!!).show {
            title(R.string.app_name)
            message(R.string.do_you_want_to_get_notified)
            positiveButton(text = activity!!.resources.getString(R.string.yes)) {
                setupViewModel.updateNotificationFlags()
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
                it.dismiss()
            }
            negativeButton(text = activity!!.resources.getString(R.string.no)) {
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
                it.dismiss()
            }
        }
    }

    override fun initializeComponents() {
        btnRetry.setOnClickListener {
            setupViewModel.loadListOfCountries()
        }
    }

    override fun observeTheLiveData() {
        setupViewModel.countriesList.observe(this, Observer {
            countryAdapter = CountryAdapter(it, this)
            rvCountries.adapter = countryAdapter
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

}