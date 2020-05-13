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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.uw.cs403.plantmap.R
import edu.uw.cs403.plantmap.RequestQueueSingleton
import org.json.JSONObject

class RegisterPlantActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val NO_PICTURE_TEXT = "Please add an image"
    val NO_LOCATION_TEXT = "Please enable location services"
    val NO_CAMERA_TEXT = "Please enable camera"
    val DURATION = Toast.LENGTH_SHORT

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
                val description = descriptionEditText.text.toString();
                val name = nameEditText.text.toString()
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        if (location == null) {
                            Toast.makeText(this, NO_LOCATION_TEXT, DURATION).show()
                        } else {
                            // TODO: send image to controller to be registered
                            val baseURL = "https://plantmap.herokuapp.com/v1/"
                            val entity1 = HashMap<Any?, Any?>()
                            entity1["name"] = name
                            entity1["description"] = description
                            val plantPostRequest =
                                JsonObjectRequest(Request.Method.POST, baseURL + "plant", JSONObject(entity1),
                                Response.Listener { response ->
                                    val plantId = response.get("plant_id")

                                    val entity2 = HashMap<Any?, Any?>()
                                    entity2["plant_id"] = plantId
                                    entity2["latitude"] = location.latitude.toFloat()
                                    entity2["longitude"] = location.longitude.toFloat()
                                    entity2["posted_on"] = System.currentTimeMillis()
                                    entity2["posted_by"] = "Test User"

                                    val submissionPostRequest =
                                        JsonObjectRequest(Request.Method.POST, baseURL + "submission", JSONObject(entity2),
                                            Response.Listener { _ ->
                                                finish()
                                            },
                                            Response.ErrorListener { error ->
                                                // TODO: something
                                            })

                                    RequestQueueSingleton.getInstance(this).addToRequestQueue(submissionPostRequest)
                                },
                                Response.ErrorListener { error ->
                                    // TODO: something
                                })
                            RequestQueueSingleton.getInstance(this).addToRequestQueue(plantPostRequest)

                        }
                    }
            }
        }

        cancelButton.setOnClickListener { _ ->
            finish()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            plantImageView.setImageBitmap(imageBitmap)
            image = imageBitmap
        }
    }

    private fun locationEnabled() =
        (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)

    private fun cameraEnabled() =
        packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
}