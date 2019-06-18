package com.stavro_xhardha.pockettreasure.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.*
import com.stavro_xhardha.pockettreasure.alarm.PrayerAlarmReceiver
import com.stavro_xhardha.pockettreasure.alarm.PrayerTimeAlarm
import com.stavro_xhardha.pockettreasure.brain.*
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
        //initPrayerWorker()
        askForNotifyingUser()
    }

    private fun askForNotifyingUser() {
        MaterialDialog(activity!!).show {
            title(R.string.app_name)
            message(R.string.do_you_want_to_get_notified)
            positiveButton(text = activity!!.resources.getString(R.string.yes)) {
                setupViewModel.updateNotificationFlags()
                setupViewModel.scheduleSynchronisationTime()
                it.dismiss()
            }
            negativeButton(text = activity!!.resources.getString(R.string.no)) {
                it.dismiss()
            }
        }
    }

    override fun initializeComponents() {}

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

        setupViewModel.tomorowsTime.observe(this, Observer {
            scheduleAlarm(activity!!, it, PENDING_INTENT_SYNC, PrayerAlarmReceiver::class.java)
            findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
        })

        setupViewModel.fajrTime.observe(this, Observer {
            scheduleAlarm(activity!!, it, PENDING_INTENT_FIRE_NOTIFICATION_FAJR, PrayerTimeAlarm::class.java)
        })

        setupViewModel.dhuhrTime.observe(this, Observer {
            scheduleAlarm(activity!!, it, PENDING_INTENT_FIRE_NOTIFICATION_DHUHR, PrayerTimeAlarm::class.java)
        })

        setupViewModel.asrTime.observe(this, Observer {
            scheduleAlarm(activity!!, it, PENDING_INTENT_FIRE_NOTIFICATION_ASR, PrayerTimeAlarm::class.java)
        })

        setupViewModel.maghribTime.observe(this, Observer {
            scheduleAlarm(activity!!, it, PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB, PrayerTimeAlarm::class.java)
        })

        setupViewModel.ishaTime.observe(this, Observer {
            scheduleAlarm(activity!!, it, PENDING_INTENT_FIRE_NOTIFICATION_ISHA, PrayerTimeAlarm::class.java)
        })
    }

    override fun handleOnBackPressed(view: View) {
    }
}
