package com.stavro_xhardha.pockettreasure.ui.setup

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import java.util.*
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

    override fun initializeComponents() {

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

        setupViewModel.tomorowsTime.observe(this, Observer {
            startSchedulingNotifications(it)
            findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
        })

        setupViewModel.fajrTime.observe(this, Observer {
            setAlarm(it, PENDING_INTENT_FIRE_NOTIFICATION_FAJR)
        })

        setupViewModel.dhuhrTime.observe(this, Observer {
            setAlarm(it, PENDING_INTENT_FIRE_NOTIFICATION_DHUHR)
        })

        setupViewModel.asrTime.observe(this, Observer {
            setAlarm(it, PENDING_INTENT_FIRE_NOTIFICATION_ASR)
        })

        setupViewModel.maghribTime.observe(this, Observer {
            setAlarm(it, PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB)
        })

        setupViewModel.ishaTime.observe(this, Observer {
            setAlarm(it, PENDING_INTENT_FIRE_NOTIFICATION_ISHA)
        })
    }

    private fun startSchedulingNotifications(time: Calendar) {
        val intent = Intent(activity, PrayerAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                activity,
                PENDING_INTENT_SYNC,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        scheduleAlarm(time, alarmManager, pendingIntent)
    }

    private fun setAlarm(fajrTime: Calendar?, intentKey: Int) {
        val intent = Intent(activity, PrayerTimeAlarm::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                activity,
                intentKey,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        scheduleAlarm(fajrTime!!, alarmManager, pendingIntent)
    }

    override fun handleOnBackPressed(view: View) {
    }
}
