package com.stavro_xhardha.pockettreasure.ui.setup


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject

class SetupFragment : BaseFragment() {

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
        rvCountries.layoutManager = LinearLayoutManager(activity)
    }

    override fun performDi() {
        DaggerSetupComponent.builder().setupFragmentModule(SetupFragmentModule(context!!))
            .pocketTreasureComponent(((activity!!.application) as PocketTreasureApplication).getPocketTreasureComponent())
            .build().inject(this)
    }

    override fun initViewModel() {
        setupViewModel = ViewModelProviders.of(this, setupViewModelFactory).get(SetupViewModel::class.java)
        setupViewModel.countriesList.observe(this, Observer {
            countriesAdapter = CountriesAdapter(it)
            rvCountries.adapter = countriesAdapter
        })
    }

}
