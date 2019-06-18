package com.stavro_xhardha.pockettreasure.ui.tasbeeh


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.getBackToHomeFragment

class TasbeehFragment : BaseFragment() {

    private lateinit var tasbeehViewModel: TasbeehViewModel

    override fun initializeComponents() {
    }

    override fun initViewModel() {
        tasbeehViewModel = ViewModelProviders.of(this).get(TasbeehViewModel::class.java)
    }

    override fun performDi() {
    }

    override fun observeTheLiveData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasbeeh, container, false)
    }

    override fun handleOnBackPressed(view: View) {
        getBackToHomeFragment(view, requireActivity(), this)
    }

}
