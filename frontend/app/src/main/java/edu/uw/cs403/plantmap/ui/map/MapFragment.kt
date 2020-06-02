package edu.uw.cs403.plantmap.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Response
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.uw.cs403.plantmap.R
import edu.uw.cs403.plantmap.clients.BackendClient
import edu.uw.cs403.plantmap.clients.RequestQueueSingleton
import edu.uw.cs403.plantmap.clients.UWPlantMapClient
import edu.uw.cs403.plantmap.ui.RegisterSubmissionActivity
import edu.uw.cs403.plantmap.ui.ViewSubmissionActivity

/**
 * Fragment that represents the main page of the app. This contains the map (with plant locations),
 * a search bar, and a button to register a new plant
 */
class MapFragment : Fragment(), OnMapReadyCallback {
    // Bounds for map camera
    private val UW_BOUNDS =
        LatLngBounds(LatLng(47.647453, -122.314609), LatLng(47.662556, -122.298299))
    // Coordinates to center map camera at
    private val UW_COORDS = LatLng(47.656021, -122.307156)
    private val ZOOM = 15f

    private val markers = HashMap<Marker, Int>()

    private lateinit var client: UWPlantMapClient

    private lateinit var mapViewModel: MapViewModel
    private lateinit var mapView: MapView
    private lateinit var registerFAB : FloatingActionButton
    private lateinit var UWMap: GoogleMap

    /**
     * Initializes view. Sets layout, initializes widgets, and sets listeners
     *
     * @param inflater LayoutInflater that can be used to inflate a view in this fragment
     * @param container The parent view for this fragment
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_map, container, false)

        // Get widgets
        mapView = root.findViewById(R.id.mapView)
        registerFAB = root.findViewById(R.id.registerPlantFAB)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        registerFAB.setOnClickListener { _ ->
            context!!.startActivity(Intent(context!!, RegisterSubmissionActivity::class.java))
        }

        // Get client singleton
        client =  BackendClient.getInstance(
            RequestQueueSingleton.getInstance(this.context!!.applicationContext)
        )

        return root
    }

    /**
     * Called when this fragment should save its state
     *
     * @param outState Bundle in which to place your saved state.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    /**
     * Called when the fragment is visible to the user and actively running
     */
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    /**
     * Called when the Fragment is no longer resumed.
     */
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    /**
     * Called when the Fragment is visible to the user
     */
    override fun onStart() {
        super.onStart()
        mapView.onStart()
        if (this::UWMap.isInitialized) {
            loadSubmissions()
        }
    }

    /**
     * Called when the Fragment is no longer started
     */
    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    /**
     * Called when the fragment is no longer in use
     */
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    /**
     * This is called when the overall system is running low on memory, and actively running
     * processes should trim their memory usage
     */
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    /**
     * Callback method for when the GoogleMap is ready to be used
     *
     * @param googleMap: A non-null instance of a GoogleMap associated with the MapView
     */
    override fun onMapReady(googleMap: GoogleMap) {
        // get map and set bounds
        UWMap = googleMap
        UWMap.setLatLngBoundsForCameraTarget(UW_BOUNDS)
        UWMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UW_COORDS, ZOOM))

        // disable mylocation button, and enable location on map (if we have the permission)
        UWMap.uiSettings.isMyLocationButtonEnabled = false
        if (locationEnabled()) {
            UWMap.isMyLocationEnabled = true
        }

        // setup listener
        UWMap.setOnMarkerClickListener { marker ->
            if (!markers.containsKey(marker)) {
                false
            } else {
                val intent = Intent(context!!, ViewSubmissionActivity::class.java)
                intent.putExtra("submissionId", markers[marker])
                startActivity(intent)

                true
            }
        }

        loadSubmissions()
    }

    /**
     * Sends a request for submissions, and presents them on the map if a response is received
     */
    private fun loadSubmissions() {
        for ((marker, _) in markers) {
            marker.remove()
        }

        markers.clear()

        client.getSubmissions(
            Response.Listener { submissions ->
                for (submission in submissions) {
                    val latLng = LatLng(submission.latitude!!.toDouble(), submission.longitude!!.toDouble())

                    markers[UWMap.addMarker(MarkerOptions().position(latLng))] = submission.post_id!!
                }
            },
            Response.ErrorListener { error ->
                // TODO: something
            }
        )
    }

    /**
     * True if and only if we have location permissions
     */
    private fun locationEnabled() =
        (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
}