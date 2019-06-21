package com.stavro_xhardha.pockettreasure.ui.gallery


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG
import com.stavro_xhardha.pockettreasure.brain.getBackToHomeFragment
import com.stavro_xhardha.pockettreasure.brain.isDebugMode
import com.stavro_xhardha.pockettreasure.brain.InitialNetworkState
import com.stavro_xhardha.pockettreasure.brain.CurrentNetworkStatus
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import javax.inject.Inject

class GalleryFragment : BaseFragment(), GalleryContract {

    @Inject
    lateinit var galleryViewModelFactory: GalleryViewModelFactory
    @Inject
    lateinit var galleryAdapter: GalleryAdapter

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun handleOnBackPressed(view: View) {
        getBackToHomeFragment(view, requireActivity(), this)
    }

    override fun initializeComponents() {
        rvGallery.layoutManager = GridLayoutManager(activity, 3)
        rvGallery.adapter = galleryAdapter
    }

    override fun initViewModel() {
        galleryViewModel = ViewModelProviders.of(this, galleryViewModelFactory).get(GalleryViewModel::class.java)
    }

    override fun performDi() {
        DaggerGalleryComponent.builder().pocketTreasureComponent(PocketTreasureApplication.getPocketTreasureComponent())
            .galleryModule(GalleryModule(this))
            .build().inject(this)
    }

    override fun observeTheLiveData() {
        galleryViewModel.primaryNetworkStatus.observe(this, Observer {
            when (it) {
                InitialNetworkState.ERROR -> {
                    llError.visibility = View.VISIBLE
                    rvGallery.visibility = View.GONE
                    pbGallery.visibility = View.GONE
                    if (isDebugMode)
                        Log.d(APPLICATION_TAG, "INITIAL_FAILED")
                }
                InitialNetworkState.SUCCESS -> {
                    llError.visibility = View.GONE
                    rvGallery.visibility = View.VISIBLE
                    pbGallery.visibility = View.GONE
                    if (isDebugMode)
                        Log.d(APPLICATION_TAG, "INITIAL_SUCCESS")
                }
                InitialNetworkState.LOADING -> {
                    llError.visibility = View.GONE
                    rvGallery.visibility = View.GONE
                    pbGallery.visibility = View.VISIBLE
                    if (isDebugMode)
                        Log.d(APPLICATION_TAG, "INITIAL_LOADING")
                }
                else -> Log.d(APPLICATION_TAG, "TOTAL ERROR")
            }
        })
        galleryViewModel.getUnsplashLiveData().observe(this, Observer {
            galleryAdapter.submitList(it)
        })

        galleryViewModel.currentNetworkStatus.observe(this, Observer {
            if (isDebugMode) {
                when (it) {
                    //todo fix handle the errors on the view
                    CurrentNetworkStatus.FAILED -> Log.d(APPLICATION_TAG, "FAILED")
                    CurrentNetworkStatus.SUCCESS -> Log.d(APPLICATION_TAG, "SUCCESS")
                    CurrentNetworkStatus.LOADING -> Log.d(APPLICATION_TAG, "LOADING")
                    else -> Log.d(APPLICATION_TAG, "TOTAL ERROR")
                }
            }
        })
    }

    override fun onImageHolderClicked(url: String) {
        if (url.isNotEmpty()) {
            val action = GalleryFragmentDirections.actionGalleryFragmentToFullImageFragment(url)
            findNavController().navigate(action)
        }
    }
}