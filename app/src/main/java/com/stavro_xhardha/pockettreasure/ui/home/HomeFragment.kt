package com.stavro_xhardha.pockettreasure.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.loadPrayerTimes()
    }

    override fun performDi() {
        DaggerHomeComponent.builder()
            .pocketTreasureComponent(PocketTreasureApplication.getPocketTreasureComponent())
            .build()
            .inject(this)
    }

    override fun initViewModel() {
        homeViewModel = ViewModelProviders.of(this, homeViewModelFactory).get(HomeViewModel::class.java)
    }

    override fun initializeComponents() {
//        val intent = Intent(activity, PrayerAlarmReceiver::class.java)
        //todo drop this and please refactor now or latter is gonna get worse
//        val pendingIntent =
//            PendingIntent.getBroadcast(activity, PENDING_INTENT_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        val calendar = Calendar.getInstance().apply {
//            set(Calendar.HOUR_OF_DAY, 20)
//            set(Calendar.MINUTE, 53)
//            set(Calendar.SECOND, 0)
//        }
//
//        alarmManager.setExact(
//            AlarmManager.RTC,
//            calendar.timeInMillis,
//            pendingIntent
//        )
    }

    override fun observeTheLiveData() {

        observeTiming()

        observeColors()

        homeViewModel.locationSecton.observe(this, Observer {
            tvCurrentLocation.text = it
        })

        homeViewModel.monthSection.observe(this, Observer {
            tvDateTime.text = it
        })

        homeViewModel.showErroToast.observe(this, Observer {
            if (it) Toast.makeText(activity!!, R.string.error_occured, Toast.LENGTH_LONG).show()
        })

        homeViewModel.progressBarVisibility.observe(this, Observer {
            pbHome.visibility = it
        })

        homeViewModel.contentVisibility.observe(this, Observer {
            rlHomeContentHolder.visibility = it
        })
    }

    private fun observeColors() {
        homeViewModel.fajrColor.observe(this, Observer {
            cvFajr.setBackgroundColor(ContextCompat.getColor(context!!, it))
        })

        homeViewModel.dhuhrColor.observe(this, Observer {
            cvDhuhr.setBackgroundColor(ContextCompat.getColor(context!!, it))
        })

        homeViewModel.asrColor.observe(this, Observer {
            cvAsr.setBackgroundColor(ContextCompat.getColor(context!!, it))
        })

        homeViewModel.maghribColor.observe(this, Observer {
            cvMaghrib.setBackgroundColor(ContextCompat.getColor(context!!, it))
        })

        homeViewModel.ishaColor.observe(this, Observer {
            cvIsha.setBackgroundColor(ContextCompat.getColor(context!!, it))
        })
    }

    private fun observeTiming() {
        homeViewModel.fajrTime.observe(this, Observer {
            tvFajrTime.text = it
        })

        homeViewModel.dhuhrtime.observe(this, Observer {
            tvDhuhrTime.text = it
        })

        homeViewModel.asrTime.observe(this, Observer {
            tvAsrTime.text = it
        })

        homeViewModel.maghribTime.observe(this, Observer {
            tvMagribTime.text = it
        })

        homeViewModel.ishaTime.observe(this, Observer {
            tvIshaTime.text = it
        })
    }

    override fun handleOnBackPressed(view: View) {
    }
}
