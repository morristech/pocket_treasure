package com.stavro_xhardha.pockettreasure.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun performDi() {
        DaggerHomeComponent.builder().pocketTreasureComponent(
            (activity!!.application as PocketTreasureApplication).getPocketTreasureComponent()
        ).build().inject(this)
    }

    override fun initViewModel() {
        homeViewModel = ViewModelProviders.of(this, homeViewModelFactory).get(HomeViewModel::class.java)
    }
}
