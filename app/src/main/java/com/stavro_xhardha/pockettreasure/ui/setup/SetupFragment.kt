package com.stavro_xhardha.pockettreasure.ui.setup

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG
import com.stavro_xhardha.pockettreasure.brain.REQUEST_CHECK_LOCATION_SETTINGS
import com.stavro_xhardha.pockettreasure.brain.REQUEST_LOCATION_PERMISSION
import com.stavro_xhardha.pockettreasure.brain.isDebugMode
import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureViewModelFactory
import com.stavro_xhardha.pockettreasure.ui.SharedViewModel
import java.util.*
import javax.inject.Inject

class SetupFragment : BaseFragment() {

    @Inject
    lateinit var factory: PocketTreasureViewModelFactory

    private lateinit var setupViewModel: SetupViewModel
    private var locationRequest: LocationRequest? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    override fun performDi() {
        component.inject(this)
    }

    override fun initViewModel() {
        setupViewModel = ViewModelProviders.of(this, factory).get(SetupViewModel::class.java)
        sharedViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        }
    }

    private fun startLocationRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), REQUEST_LOCATION_PERMISSION
                )
            } else {
                getUserLocation()
            }
        } else {
            getUserLocation()
        }
    }

    private fun getUserLocation() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            if (isDebugMode) Log.d(APPLICATION_TAG, "LOCATION SETTINGS ARE READY, GPS IS ON")
            updateLocation()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        requireActivity(),
                        REQUEST_CHECK_LOCATION_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    private fun updateLocation() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    requireActivity().finish()
                } else {
                    for (location in locationResult.locations) {
                        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                        setupViewModel.convertToAdress(geocoder, location.latitude, location.longitude)
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), REQUEST_LOCATION_PERMISSION
                )
            } else {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            }
        } else {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    requireActivity().finish()
                }
                return
            }
        }
    }

    private fun askForNotifyingUser() {
        MaterialDialog(activity!!).show {
            title(R.string.app_name)
            message(R.string.do_you_want_to_get_notified)
            positiveButton(text = activity!!.resources.getString(R.string.yes)) {
                setupViewModel.updateNotificationFlags()
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
                it.dismiss()
            }
            negativeButton(text = activity!!.resources.getString(R.string.no)) {
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
                it.dismiss()
            }
            onDismiss {
                Toast.makeText(requireActivity(), R.string.settings_saved, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun initializeComponents() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun observeTheLiveData() {
        setupViewModel.locationSettingsRequest.observe(this, Observer {
            if (it) startLocationRequest()
        })
        setupViewModel.locationRequestTurnOff.observe(this, Observer {
            if (it) fusedLocationClient.removeLocationUpdates(locationCallback)
        })
        setupViewModel.locationerrorVisibility.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), R.string.invalid_coorinates, Toast.LENGTH_LONG).show()
                requireActivity().finish()
            }
        })
        setupViewModel.serviceNotAvailableVisibility.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), R.string.service_not_available, Toast.LENGTH_LONG).show()
                requireActivity().finish()
            }
        })
        setupViewModel.prayerNotificationDialogViaibility.observe(this, Observer {
            if (it) askForNotifyingUser()
        })
        setupViewModel.locationProvided.observe(this, Observer {
            if (it)
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToHomeFragment3())
        })
        sharedViewModel.onGpsOpened.observe(this, Observer {
            if (it) updateLocation()
        })
    }
}