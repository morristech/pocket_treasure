package com.stavro_xhardha.pockettreasure

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    //started from the bottom

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        performDi()
        initializeComponents()
        initViewModel()
        observeTheLiveData()
    }

    abstract fun initializeComponents()

    abstract fun initViewModel()

    abstract fun performDi()

    abstract fun observeTheLiveData()

    //just kidding, this is the bottom :)
}