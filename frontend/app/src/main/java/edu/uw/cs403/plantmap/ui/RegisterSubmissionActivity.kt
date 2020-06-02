package edu.uw.cs403.plantmap.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.volley.Response
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.uw.cs403.plantmap.R
import edu.uw.cs403.plantmap.clients.BackendClient
import edu.uw.cs403.plantmap.clients.RequestQueueSingleton
import edu.uw.cs403.plantmap.clients.UWPlantMapClient
import java.io.File
import java.io.IOException

/**
 * Activity representing the flow for registering a new submission
 */
class RegisterSubmissionActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val NO_PICTURE_TEXT = "Please add an image"
    val NO_LOCATION_TEXT = "Please enable location services"
    val NO_CAMERA_TEXT = "Please enable camera"
    val NETWORK_ERROR_TEXT = "Could not upload submission: "
    val FILE_SAVE_TEXT = "Could not save image file"
    val DURATION = Toast.LENGTH_SHORT
    val SUBMITTING_TEXT = "Uploading plant submission..."

    private lateinit var client: UWPlantMapClient

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var addImageButton: ImageButton
    private lateinit var registerButton: Button
    private lateinit var cancelButton: Button
    private lateinit var plantImageView: ImageView
    private lateinit var descriptionEditText: EditText
    private lateinit var nameEditText: EditText

    private lateinit var currentPhotoPath: String
    private lateinit var image: Bitmap

    /**
     * Initializes this activity. Sets layout, initializes widgets, and sets listeners
     *
     * @param savedInstanceState: If the activity is being re-initialized after previously being
     * shut down then this Bundle contains the data it most recently supplied in
     */
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
                                error.printStackTrace()
                                val errorMsg = NETWORK_ERROR_TEXT + error.networkResponse.statusCode
                                Toast.makeText(this, errorMsg, DURATION).show()
                                error.stackTrace
                            }

                            // Set up callback function for a successful submission post
                            val submissionResponseListener = Response.Listener<Int> { _ ->
                                finish()
                            }

                            // Set up a callback function for a successful plant post
                            val plantResponseListener = Response.Listener<Int> { plantId ->
                                client.postSubmission(plantId, location.latitude.toFloat(),
                                    location.longitude.toFloat(), System.currentTimeMillis(),
                                    "Test User", image, submissionResponseListener,
                                    errorListener)
                            }

                            Toast.makeText(this, SUBMITTING_TEXT, DURATION).show()
                            client.postPlant(name, description, plantResponseListener, errorListener)
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
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Toast.makeText(this, FILE_SAVE_TEXT, DURATION).show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "edu.uw.cs403.plantmap.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    /**
     * Callback method for when a picture is taken. Stores image in a temporary file if activity
     * completed successfully. Else, does nothing
     *
     * @param requestCode: request code indicating the request that this result is from
     * @param resultCode: result code indicating the status of the result
     * @param data: Intent storing data to be returned (stored as an extra)
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            image = BitmapFactory.decodeFile(currentPhotoPath)
            plantImageView.setImageBitmap(image)
        }
    }

    /**
     * Creates and returns a temporary file to store an image in (jpg)
     *
     * @return a File object representing the newly created file
     */
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_submission_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    /**
     * @return true iff location permissions are enabled
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