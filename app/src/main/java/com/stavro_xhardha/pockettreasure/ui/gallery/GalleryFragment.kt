package com.stavro_xhardha.pockettreasure.ui.gallery


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.Status
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import javax.inject.Inject

class GalleryFragment : BaseFragment(), GalleryContract {

    @Inject
    lateinit var galleryViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var galleryAdapter: GalleryAdapter

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }
        })
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
        AndroidSupportInjection.inject(this)
    }

    override fun observeTheLiveData() {
        galleryViewModel.getGalleryLiveData().observe(this, Observer {
            galleryAdapter.submitList(it)
        })
        galleryViewModel.getCurrentState().observe(this, Observer {
            if (it.status == Status.FAILED)
                Snackbar.make(rlGallery, R.string.failed_loading_more, Snackbar.LENGTH_LONG).show()
        })
        galleryViewModel.getInitialState().observe(this, Observer {
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
        })
    }

    override fun onImageHolderClicked(url: String) {
        if (url.isNotEmpty()) {
            val action = GalleryFragmentDirections.actionGalleryFragmentToFullImageFragment(url)
            findNavController().navigate(action)
        }
    }
}