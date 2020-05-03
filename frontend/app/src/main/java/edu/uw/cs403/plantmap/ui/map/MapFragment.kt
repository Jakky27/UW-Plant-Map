package edu.uw.cs403.plantmap.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import edu.uw.cs403.plantmap.R


class MapFragment : Fragment(), OnMapReadyCallback {
    private val UW_BOUNDS =
        LatLngBounds(LatLng(47.647453, -122.314609), LatLng(47.662556, -122.298299))
    private val UW_COORDS = LatLng(47.656021, -122.307156)
    private val ZOOM = 15f

    private lateinit var mapViewModel: MapViewModel

    private lateinit var searchView: SearchView
    private lateinit var mapView: MapView

    private lateinit var UWMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialize widgets
        searchView = root.findViewById(R.id.mapSearch)
        mapView = root.findViewById(R.id.mapView)


        initSearchBarCallbacks()
        initMap(savedInstanceState)

        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun initSearchBarCallbacks() {
        // Open and closing searchbar
        searchView.setOnQueryTextFocusChangeListener { view, b ->
            Log.d("DEBUG", "Focus Toggled")
        }
        //
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("DEBUG", "Query Changed")
                Log.d("DEBUG", searchView.query.toString())
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("DEBUG", "Query Submitted")
                return true
            }
        })
    }

    private fun initMap(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        UWMap = googleMap
        UWMap.setLatLngBoundsForCameraTarget(UW_BOUNDS)
        UWMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UW_COORDS, ZOOM))
        UWMap.uiSettings.setMyLocationButtonEnabled(false)

        if (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            UWMap.setMyLocationEnabled(true)
        }

        //TODO: Load in plant submissions
    }
}