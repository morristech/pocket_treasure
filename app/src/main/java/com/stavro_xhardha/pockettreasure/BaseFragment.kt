package com.stavro_xhardha.pockettreasure

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

abstract class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        performDi()
        initializeComponent()
        initViewModel()
        observeTheLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleOnBackPressed(view)
    }

    abstract fun handleOnBackPressed(view: View)

    abstract fun initializeComponent()

    abstract fun initViewModel()

    abstract fun performDi()

    abstract fun observeTheLiveData()
}