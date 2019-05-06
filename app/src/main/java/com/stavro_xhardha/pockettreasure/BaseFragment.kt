package com.stavro_xhardha.pockettreasure

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        performDi()
        initializeComponent()
        initViewModel()
        observeTheLiveData()
    }

    abstract fun initializeComponent()

    abstract fun initViewModel()

    abstract fun performDi()

    abstract fun observeTheLiveData()
}