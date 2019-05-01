package com.stavro_xhardha.pockettreasure

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        performDi()
        initViewModel()
    }

    abstract fun initViewModel()

    abstract fun performDi()
}