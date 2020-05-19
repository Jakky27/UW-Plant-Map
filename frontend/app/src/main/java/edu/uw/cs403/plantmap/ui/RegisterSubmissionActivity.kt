package edu.uw.cs403.plantmap.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.uw.cs403.plantmap.R
import edu.uw.cs403.plantmap.clients.BackendClient
import edu.uw.cs403.plantmap.clients.RequestQueueSingleton
import edu.uw.cs403.plantmap.clients.UWPlantMapClient
import org.json.JSONObject

/**
 * Activity representing the flow for registering a new submission
 */
class RegisterSubmissionActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val NO_PICTURE_TEXT = "Please add an image"
    val NO_LOCATION_TEXT = "Please enable location services"
    val NO_CAMERA_TEXT = "Please enable camera"
    val NETWORK_ERROR_TEXT = "Could not upload submission: "
    val DURATION = Toast.LENGTH_SHORT

    private lateinit var client: UWPlantMapClient

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var addImageButton: ImageButton
    private lateinit var registerButton: Button
    private lateinit var cancelButton: Button
    private lateinit var plantImageView: ImageView
    private lateinit var descriptionEditText: EditText
    private lateinit var nameEditText: EditText

    private lateinit var image : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Get widgets
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        addImageButton = findViewById(R.id.registerAddImage)
        registerButton = findViewById(R.id.registerButton)
        cancelButton = findViewById(R.id.registerCancelButton)
        descriptionEditText = findViewById(R.id.registerDescriptionEditText)
        nameEditText = findViewById(R.id.registerPlantNameEditText)
        plantImageView = findViewById(R.id.plantImageView)

        addImageButton.setOnClickListener { _ ->
            if (cameraEnabled()) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, NO_CAMERA_TEXT, DURATION).show()
            }
        }

        registerButton.setOnClickListener { _ ->
            if (!this::image.isInitialized) {
                Toast.makeText(this, NO_PICTURE_TEXT, DURATION).show()
            } else if (!locationEnabled()) {
                Toast.makeText(this, NO_LOCATION_TEXT, DURATION).show()
            } else {
                // Fetch description and name from text boxes
                val description = descriptionEditText.text.toString();
                val name = nameEditText.text.toString()

                // Attempt to get location asynchronously
                fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                        if (location == null) {
                            Toast.makeText(this, NO_LOCATION_TEXT, DURATION).show()
                        } else {
                            // Set up callback function for errors
                            val errorListener = Response.ErrorListener { error ->
                                val errorMsg = NETWORK_ERROR_TEXT + error.networkResponse.statusCode
                                Toast.makeText(this, errorMsg, DURATION)
                                error.stackTrace
                            }

                            // Set up callback function for a successful submission post
                            val submissionResponseListener = Response.Listener<Int> { _ ->
                                // TODO: send image to controller to be registered
                            }

                            // Set up a callback function for a successful plant post
                            val plantResponseListener = Response.Listener<Int> { plantId ->
                                client.postSubmission(plantId, location.latitude.toFloat(),
                                    location.longitude.toFloat(), System.currentTimeMillis(),
                                    "Test User", submissionResponseListener, errorListener)
                            }

                            client.postPlant(name, description, plantResponseListener, errorListener)

                            // Return back to home
                            finish()
                        }
                    }
            }
        }

        // get client singleton
        client = BackendClient.getInstance(RequestQueueSingleton.getInstance(this.applicationContext))

        cancelButton.setOnClickListener { _ ->
            // return home
            finish()
        }
    }

    /**
     * Create a new intent for taking a picture
     */
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    /**
     * Callback method for when a picture is taken
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            // Update photo imageview and store bitmap
            plantImageView.setImageBitmap(imageBitmap)
            image = imageBitmap
        }
    }

    /**
     * @return true iff location is enabled
     */
    private fun locationEnabled() =
        (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)

    /**
     * @return true iff location is enabled
     */
    private fun cameraEnabled() =
        packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
}