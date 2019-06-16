package com.stavro_xhardha.pockettreasure.ui.setup


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.afollestad.materialdialogs.MaterialDialog
import com.crashlytics.android.Crashlytics
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.MainActivity
import com.stavro_xhardha.pockettreasure.PrayerAlarmReceiver
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG
import com.stavro_xhardha.pockettreasure.brain.PENDING_INTENT_CODE
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.worker.PrayerWorker
import kotlinx.android.synthetic.main.fragment_setup.*
import java.util.*
import java.util.concurrent.TimeUnit
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

        setupViewModel.calendar.observe(this, Observer {
            startSchedulingNotifications(it)
            findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
        })
    }

    private fun startSchedulingNotifications(midnightTime: Calendar) {

        val intent = Intent(activity, PrayerAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(activity, PENDING_INTENT_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(
            AlarmManager.RTC,
            midnightTime.timeInMillis,
            pendingIntent
        )
    }

    override fun handleOnBackPressed(view: View) {
    }

    private fun initPrayerWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWork =
            PeriodicWorkRequest.Builder(
                PrayerWorker::class.java,
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.SECONDS
            )
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance().enqueue(periodicWork)
    }

}
