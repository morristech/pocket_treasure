package com.stavro_xhardha.pockettreasure.ui.settings


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
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureViewModelFactory
import com.stavro_xhardha.pockettreasure.ui.SharedViewModel
import com.stavro_xhardha.pockettreasure.ui.setup.SetupFragmentDirections
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*
import javax.inject.Inject

class SettingsFragment : BaseFragment() {
    @Inject
    lateinit var factory: PocketTreasureViewModelFactory

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var locationRequest: LocationRequest? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        swFajr.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwFajrClick(isChecked)
        }

        swDhuhr.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwDhuhrClick(isChecked)
        }

        swAsr.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwAsrClick(isChecked)
        }

        swMaghrib.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwMaghribClick(isChecked)
        }

        swIsha.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwIshaClick(isChecked)
        }

        llLocation.setOnClickListener {
            remakeLocationRequest()
        }
    }

    private fun remakeLocationRequest() {
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
                    Toast.makeText(requireActivity(), R.string.error_occured, Toast.LENGTH_LONG).show()
                } else {
                    for (location in locationResult.locations) {
                        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                        settingsViewModel.convertToAdress(geocoder, location.latitude, location.longitude)
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
                    Toast.makeText(requireActivity(), R.string.values_cannot_be_updated, Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun initViewModel() {
        settingsViewModel = ViewModelProviders.of(this, factory).get(SettingsViewModel::class.java)

        sharedViewModel = requireActivity().run {
            ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
        }
    }

    override fun performDi() {
        component.inject(this)
    }

    override fun observeTheLiveData() {
        settingsViewModel.fajrCheck.observe(this, Observer {
            swFajr.isChecked = it
        })
        settingsViewModel.dhuhrCheck.observe(this, Observer {
            swDhuhr.isChecked = it
        })
        settingsViewModel.asrCheck.observe(this, Observer {
            swAsr.isChecked = it
        })
        settingsViewModel.maghribCheck.observe(this, Observer {
            swMaghrib.isChecked = it
        })
        settingsViewModel.ishaCheck.observe(this, Observer {
            swIsha.isChecked = it
        })
        settingsViewModel.countryAndCapital.observe(this, Observer {
            tvCountryAndCapital.text = it
        })
        settingsViewModel.locationSettingsRequest.observe(this, Observer {
            if (it) remakeLocationRequest()
        })
        settingsViewModel.locationRequestTurnOff.observe(this, Observer {
            if (it) fusedLocationClient.removeLocationUpdates(locationCallback)
        })
        settingsViewModel.locationerrorVisibility.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), R.string.invalid_coorinates, Toast.LENGTH_LONG).show()
            }
        })
        settingsViewModel.serviceNotAvailableVisibility.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), R.string.service_not_available, Toast.LENGTH_LONG).show()
            }
        })
        sharedViewModel.onGpsOpened.observe(this, Observer {
            if (it) updateLocation()
        })
        settingsViewModel.workManagerReadyToStart.observe(this, Observer {
            if (it) {
                startWorkManager(requireActivity())
                Toast.makeText(requireActivity(), R.string.location_updated_successfully, Toast.LENGTH_LONG).show()
            }
        })
    }
}