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
import com.google.android.material.snackbar.Snackbar
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG
import com.stavro_xhardha.pockettreasure.brain.Status
import com.stavro_xhardha.pockettreasure.brain.getBackToHomeFragment
import com.stavro_xhardha.pockettreasure.brain.isDebugMode
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
        btnRetry.setOnClickListener {
            galleryViewModel.retry()
        }
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
        galleryViewModel.getGalleryLiveData().observe(this, Observer {
            galleryAdapter.submitList(it)
        })
        galleryViewModel.getCurrentState().observe(this, Observer {
            if (isDebugMode) {
                when (it.status) {
                    Status.FAILED -> {
                        Snackbar.make(rlGallery, R.string.failed_loading_more, Snackbar.LENGTH_LONG).show()
                    }
                    Status.RUNNING -> {
                        Log.d(APPLICATION_TAG, "LOADING")
                    }
                    Status.SUCCESS -> {
                        Log.d(APPLICATION_TAG, "SUCCESS")
                    }
                }
            }
        })
        galleryViewModel.getInitialState().observe(this, Observer {
            if (isDebugMode) {
                when (it.status) {
                    Status.FAILED -> {
                        pbGallery.visibility = View.GONE
                        llError.visibility = View.VISIBLE
                        rvGallery.visibility = View.GONE
                    }
                    Status.RUNNING -> {
                        pbGallery.visibility = View.VISIBLE
                        llError.visibility = View.GONE
                        rvGallery.visibility = View.GONE
                    }
                    Status.SUCCESS -> {
                        pbGallery.visibility = View.GONE
                        llError.visibility = View.GONE
                        rvGallery.visibility = View.VISIBLE
                    }
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