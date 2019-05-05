package com.stavro_xhardha.pockettreasure.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun performDi() {
        DaggerHomeComponent.builder().pocketTreasureComponent(
            (activity!!.application as PocketTreasureApplication).getPocketTreasureComponent()
        ).build().inject(this)
    }

    override fun initViewModel() {
        homeViewModel = ViewModelProviders.of(this, homeViewModelFactory).get(HomeViewModel::class.java)

        homeViewModel.fejrTime.observe(this, Observer {
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

        homeViewModel.locationSecton.observe(this, Observer {
            tvCurrentLocation.text = it
        })

        homeViewModel.monthSection.observe(this, Observer {
            tvDateTime.text = it
        })

        homeViewModel.currentPrayerValue.observe(this, Observer {
            when (it) {
                0 -> {
                    cvFajr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))
                    cvDhuhr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvAsr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvMaghrib.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvIsha.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                }
                1 -> {
                    cvFajr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvDhuhr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))
                    cvAsr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvMaghrib.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvIsha.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                }
                2 -> {
                    cvFajr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvDhuhr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvAsr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))
                    cvMaghrib.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvIsha.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                }
                3 -> {
                    cvFajr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvDhuhr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvAsr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvMaghrib.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))
                    cvIsha.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                }
                4 -> {
                    cvFajr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvDhuhr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvAsr.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvMaghrib.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_white_1000))
                    cvIsha.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))
                }
            }
        })
    }
}
