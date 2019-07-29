package com.stavro_xhardha.pockettreasure

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.ui.SharedViewModel
import com.stavro_xhardha.rocket.Rocket
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), AppBarConfiguration.OnNavigateUpListener {
    //todo refactor

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var rocket: Rocket
    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)
    private var locationRequest: LocationRequest? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController

        setupNavigation(navController)

        setupActionBar(navController, appBarConfiguration)

        setupNavControllerListener(navController)

        //   checkLocationSettings()
    }

    private fun checkLocationSettings() {
        coroutineScope.launch {
            if (rocket.readFloat(LATITUDE_KEY) == 0.toFloat() && rocket.readFloat(LONGITUDE_KEY) == 0.toFloat()) {
                withContext(Dispatchers.IO) {
                    checkForLocationSettings()
                }
            }
        }
    }

    private fun checkForLocationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    finish()
                }
                return
            }
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

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            if (isDebugMode) Log.d(APPLICATION_TAG, "LOCATION SETTINGS ARE READY, GPS IS ON")
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        this@MainActivity,
                        REQUEST_CHECK_LOCATION_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    private fun convertToAdress(latitude: Double, longitude: Double) {
        coroutineScope.launch {
            val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
            try {
                val adresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (adresses.size != 0) {
                    val cityName = adresses[0].adminArea
                    val country = adresses[0].countryName
                    rocket.writeString(COUNTRY_SHARED_PREFERENCE_KEY, country)
                    rocket.writeString(CAPITAL_SHARED_PREFERENCES_KEY, cityName)
                    rocket.writeBoolean(COUNTRY_UPDATED, true)
                    rocket.writeFloat(LATITUDE_KEY, latitude.toFloat())
                    rocket.writeFloat(LONGITUDE_KEY, longitude.toFloat())
                    fusedLocationClient.removeLocationUpdates(locationCallback)
                    withContext(Dispatchers.IO) {
                        askForPrayerNotifications()
                    }
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
                Toast.makeText(this@MainActivity, R.string.service_not_available, Toast.LENGTH_LONG).show()
                rocket.writeFloat(LATITUDE_KEY, 0.toFloat())
                rocket.writeFloat(LONGITUDE_KEY, 0.toFloat())
                finish()
            } catch (illegalArgumentException: IllegalArgumentException) {
                illegalArgumentException.printStackTrace()
                Toast.makeText(this@MainActivity, R.string.invalid_coorinates, Toast.LENGTH_LONG).show()
                rocket.writeFloat(LATITUDE_KEY, 0.toFloat())
                rocket.writeFloat(LONGITUDE_KEY, 0.toFloat())
                finish()
            }
        }
    }

    private fun askForPrayerNotifications() {
        MaterialDialog(this).show {
            title(R.string.app_name)
            message(R.string.do_you_want_to_get_notified)
            positiveButton(text = resources.getString(R.string.yes)) { materialDialog ->
                coroutineScope.launch {
                    rocket.writeBoolean(NOTIFY_USER_FOR_FAJR, true)
                    rocket.writeBoolean(NOTIFY_USER_FOR_DHUHR, true)
                    rocket.writeBoolean(NOTIFY_USER_FOR_ASR, true)
                    rocket.writeBoolean(NOTIFY_USER_FOR_MAGHRIB, true)
                    rocket.writeBoolean(NOTIFY_USER_FOR_ISHA, true)
                    withContext(Dispatchers.IO) {
                        materialDialog.dismiss()
                    }
                }
            }
            negativeButton(text = resources.getString(R.string.no)) {
                it.dismiss()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_LOCATION_SETTINGS -> {
                when (resultCode) {
                    RESULT_OK -> {
                        sharedViewModel.onGpsOpened()
                    }
                    RESULT_CANCELED -> {
                        finish()
                    }
                }
            }
        }
    }

    private fun setupNavControllerListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.setupFragment) {
                toolbar.visibility = View.GONE
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                toolbar.visibility = View.VISIBLE
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
            if (isDebugMode) {
                val dest: String = try {
                    resources.getResourceName(destination.id)
                } catch (e: Resources.NotFoundException) {
                    destination.id.toString()
                }
                Log.d("NavigationActivity", "Navigated to $dest")
            }
        }
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigation(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)

        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.namesFragment,
                R.id.quranFragment,
                R.id.tasbeehFragment,
                R.id.galleryFragment,
                R.id.newsFragment,
                R.id.settingsFragment,
                R.id.setupFragment
            ),
            drawerLayout
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView == null) {
            menuInflater.inflate(R.menu.activity_main_drawer, menu)
            return true
        }
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                if (findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.articleWebViewFragment
                    || findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.fullImageFragment
                    || findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.ayaFragment
                ) {
                    onBackPressed()
                } else {
                    drawer_layout.openDrawer(GravityCompat.START)
                }
            }
        }
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        completableJob.cancel()
    }
}