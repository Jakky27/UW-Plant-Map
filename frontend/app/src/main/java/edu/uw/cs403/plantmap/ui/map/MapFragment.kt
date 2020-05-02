package edu.uw.cs403.plantmap.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.uw.cs403.plantmap.R

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapViewModel: MapViewModel

    private lateinit var textView: TextView
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

        // TODO remove textView - likely unused
        textView = root.findViewById(R.id.text_map)
        mapViewModel.text.observe(this, Observer {
            textView.text = it
        })

        // Initialize widgets
        searchView = root.findViewById(R.id.searchView)
        mapView = root.findViewById(R.id.mapView)


        initSearchBarCallbacks()
        initMap()

        return root
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

    private fun initMap() {
        //Bundle mapViewBundle = null
        //mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        UWMap = googleMap!! // TODO

        // TODO temp - this is adding a marker
        val marker = LatLng(-34.0, 151.0)
        UWMap.addMarker(MarkerOptions().position(marker).title(("Test marker")))
        UWMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
    }
}