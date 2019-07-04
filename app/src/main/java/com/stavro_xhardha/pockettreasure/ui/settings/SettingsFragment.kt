package com.stavro_xhardha.pockettreasure.ui.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.ui.SharedViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

    @Inject
    lateinit var settingsFragmentFactory: SettingsFragmentFactory

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }
        })
    }

    override fun initializeComponents() {
        swFajr.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwFajrClick(isChecked)
        }

        swDhuhr.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwDhuhrClick(isChecked)
        }

        swAsr.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwAsrClick(isChecked)
        }

        swMaghrib.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwMaghribClick(isChecked)
        }

        swIsha.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwIshaClick(isChecked)
        }

        llCountryAndCapital.setOnClickListener {
            findNavController().navigate(R.id.dialogFragment)
        }
    }

    override fun initViewModel() {
        settingsViewModel =
            ViewModelProviders.of(this, settingsFragmentFactory).get(SettingsViewModel::class.java)
    }

    override fun performDi() {
        DaggerSettingsFragmentComponent.builder()
            .pocketTreasureComponent(PocketTreasureApplication.getPocketTreasureComponent())
            .build().inject(this)
    }

    override fun observeTheLiveData() {
        settingsViewModel.fajrCheck.observe(this, Observer {
            swFajr.isChecked = it
        })
        settingsViewModel.dhuhrCheck.observe(this, Observer {
            swDhuhr.isChecked = it
        })
        settingsViewModel.asrCheck.observe(this, Observer {
            swAsr.isChecked = it
        })
        settingsViewModel.maghribCheck.observe(this, Observer {
            swMaghrib.isChecked = it
        })
        settingsViewModel.ishaCheck.observe(this, Observer {
            swIsha.isChecked = it
        })
        settingsViewModel.countryAndCapital.observe(this, Observer {
            tvCountryAndCapital.text = it
        })

        sharedViewModel.updatedCountry.observe(this, Observer {
            settingsViewModel.resetDataForWorker()
            tvCountryAndCapital.text = it
        })
    }
}