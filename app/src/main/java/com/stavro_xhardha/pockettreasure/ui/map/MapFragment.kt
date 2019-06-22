package com.stavro_xhardha.pockettreasure.ui.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.getBackToHomeFragment
import kotlinx.android.synthetic.main.map_fragment.*
import java.io.IOException
import javax.inject.Inject


const val MAX_LOCATION_RESULTS = 1000
const val CITY_LOCATION_ZOOM = 10f

class MapFragment : BaseFragment(), OnMapReadyCallback {

    @Inject
    lateinit var mapViewModelFactory: MapViewModelProviderFactory

    private lateinit var viewModel: MapViewModel
    private lateinit var googleMap: GoogleMap
    private lateinit var geoCoder: Geocoder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun handleOnBackPressed(view: View) {
        getBackToHomeFragment(view, requireActivity(), this)
    }

    override fun initializeComponents() {
        if (mapView != null) {
            mapView.onCreate(null)
            mapView.onResume()
            mapView.getMapAsync(this@MapFragment)
        }
    }

    private fun invokeSearch(cityToSearch: String) {
        //val fullSearchString = "mosque+in+$cityToSearch"
        geoCoder = Geocoder(activity!!)
        try {
            val city = geoCoder.getFromLocationName(cityToSearch, 1)

            if (city.size > 0)
                zoomCameraTo(city)
            else {
                showNothingFoundToast()
                return
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showNothingFoundToast() {
        Toast.makeText(activity, R.string.city_not_found, Toast.LENGTH_LONG).show()
    }

    @Throws(Exception::class)
    private fun zoomCameraTo(city: List<Address>) {
        val currentCityAdress = city[0]

        val center = CameraUpdateFactory.newLatLng(
            LatLng(
                currentCityAdress.latitude,
                currentCityAdress.longitude
            )
        )
        val zoom = CameraUpdateFactory.zoomTo(CITY_LOCATION_ZOOM)

        googleMap.moveCamera(center)
        googleMap.animateCamera(zoom)
    }

    override fun initViewModel() {
    }

    override fun performDi() {
        DaggerMapViewModelComponent.builder()
            .pocketTreasureComponent(PocketTreasureApplication.getPocketTreasureComponent())
            .build().inject(this)
    }

    override fun observeTheLiveData() {

    }

    override fun onMapReady(map: GoogleMap?) {
        MapsInitializer.initialize(activity)
        googleMap = map!!
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        viewModel = ViewModelProviders.of(this, mapViewModelFactory).get(MapViewModel::class.java)

        viewModel.initMapData()

        viewModel.city.observe(this, Observer {
            invokeSearch(it)
        })
    }

}
