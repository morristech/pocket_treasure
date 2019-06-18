package com.stavro_xhardha.pockettreasure.ui.quran.aya


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.ui.quran.QuranFragment
import kotlinx.android.synthetic.main.fragment_aya.*
import javax.inject.Inject

class AyaFragment : BaseFragment() {

    @Inject
    lateinit var ayaFragmentFactory: AyaFragmentFactory
    private lateinit var ayaViewModel: AyaViewModel
    private lateinit var ayasAdapter: AyasAdapter

    private val args: AyaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aya, container, false)
    }

    override fun handleOnBackPressed(view: View) {
    }

    override fun initializeComponents() {
        ayasAdapter = AyasAdapter()
        rvAya.adapter = ayasAdapter
        pbAya.visibility = View.VISIBLE
    }

    override fun initViewModel() {
        ayaViewModel = ViewModelProviders.of(this, ayaFragmentFactory).get(AyaViewModel::class.java)
        val ayasNumber = args.ayasNumber
        ayaViewModel.startSuraDataBaseCall(ayasNumber)
    }

    override fun performDi() {
        DaggerAyaComponent.builder().quranComponent(QuranFragment.getComponent()).build().inject(this)
    }

    override fun observeTheLiveData() {
        ayaViewModel.ayas.observe(this, Observer {
            if (it.size > 0) {
                ayasAdapter.submitList(it)
                pbAya.visibility = View.GONE
                rvAya.visibility = View.VISIBLE
            } else {
                llError.visibility = View.VISIBLE
                rvAya.visibility = View.GONE
                pbAya.visibility = View.GONE
            }
        })
    }
}