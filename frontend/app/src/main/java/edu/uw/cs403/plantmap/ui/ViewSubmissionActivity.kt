package edu.uw.cs403.plantmap.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import edu.uw.cs403.plantmap.R
import edu.uw.cs403.plantmap.clients.BackendClient
import edu.uw.cs403.plantmap.clients.RequestQueueSingleton
import edu.uw.cs403.plantmap.clients.UWPlantMapClient
import edu.uw.cs403.plantmap.models.Plant
import edu.uw.cs403.plantmap.models.Submission
import java.text.DateFormat

class ViewSubmissionActivity : AppCompatActivity() {
    private lateinit var client: UWPlantMapClient

    private lateinit var backButton: Button
    private lateinit var reportButton: Button
    private lateinit var plantName: TextView
    private lateinit var postedBy: TextView
    private lateinit var submissionImage: ImageView
    private lateinit var plantDescription: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_submission)

        backButton = findViewById(R.id.submissionBackButton)
        reportButton = findViewById(R.id.submissionReportButton)
        plantName = findViewById(R.id.submissionPlantName)
        postedBy = findViewById(R.id.submissionPostedBy)
        submissionImage = findViewById(R.id.submissionImageView)
        plantDescription = findViewById(R.id.submissionDescription)

        client =
            BackendClient.getInstance(RequestQueueSingleton.getInstance(this.applicationContext))

        val submissionId = intent.extras!!["submissionId"] as Int

        backButton.setOnClickListener {
            finish()
        }

        val errorListener = Response.ErrorListener { error ->
            error.stackTrace
        }

        reportButton.setOnClickListener {
            client.reportSubmission(submissionId, Response.Listener {
                Toast.makeText(this, "This submission has been reported", Toast.LENGTH_SHORT).show()
            }, errorListener)
        }

        val plantResponseListener = Response.Listener<Plant> { plant ->
            plantName.text = plant.name
            plantDescription.text = plant.description
        }

        val imageResponseListener = Response.Listener<Bitmap> { image ->
            submissionImage.setImageBitmap(image)
        }

        val submissionResponseListener = Response.Listener<Submission> { submission ->
            postedBy.text = getString(
                R.string.submission_posted_by_text, submission.posted_by,
                DateFormat.getInstance().format(submission.post_date)
            )

            client.getPlant(submission.plant_id!!, plantResponseListener, errorListener)

            client.getImage(submission.post_id!!, submissionImage.scaleType, imageResponseListener, errorListener)
        }

        client.getSubmission(submissionId, submissionResponseListener, errorListener)
    }
}