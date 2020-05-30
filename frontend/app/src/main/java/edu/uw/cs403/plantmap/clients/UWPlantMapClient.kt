package edu.uw.cs403.plantmap.clients

import android.graphics.Bitmap
import android.widget.ImageView
import com.android.volley.Response
import edu.uw.cs403.plantmap.models.Plant
import edu.uw.cs403.plantmap.models.Submission

/**
 * Interface for a client to send requests regarding relevant data types
 */
interface UWPlantMapClient {

    /**
     * Sends a request to get all submissions, invokes the relevant supplied listener when a
     * response is received.
     *
     * @param listener: Listener to be invoked upon success. Will be supplied a list of Submissions
     * @param errorListener: Listener to be invoked upon failure. Will be supplied a VollyError
     * object
     */
    fun getSubmissions(listener: Response.Listener<List<Submission>>,
                       errorListener: Response.ErrorListener)

    /**
     * Sends a request to get a plant, invokes the relevant supplied listener when a response
     * is received.
     *
     * @param plantId: the id of the plant to be fetched by the database
     * @param listener: Listener to be invoked upon success. Will be supplied a Plant with the
     * given id from the database
     * @param errorListener: Listener to be invoked upon failure. Will be supplied a VollyError
     * object
     */
    fun getPlant(plantId: Int, listener: Response.Listener<Plant>,
                 errorListener: Response.ErrorListener)

    /**
     * Sends a request to post a plant, invokes the relevant supplied listener when a response
     * is received.
     *
     * @param name: name of plant species to register
     * @param description: text description of plant to be registered
     * @param listener: Listener to be invoked upon success. Will be supplied an integer, the
     * id of the newly posted plant
     * @param errorListener: Listener to be invoked upon failure. Will be supplied a VollyError
     * object
     */
    fun postPlant(name: String, description: String, listener: Response.Listener<Int>,
                  errorListener: Response.ErrorListener)

    /**
     * Sends a request to get a submission, invokes the relevant supplied listener when a response
     * is received.
     *
     * @param submissionId: the id of the submission to be fetched from the database
     * @param listener: Listener to be invoked upon success. Will be supplied a Submission with the
     * given id from the database
     * @param errorListener: Listener to be invoked upon failure. Will be supplied a VollyError
     * object
     */
    fun getSubmission(submissionId: Int, listener: Response.Listener<Submission>,
                      errorListener: Response.ErrorListener)

    /**
     * Sends a request to post a submission, invokes the relevant supplied listener when a response
     * is received.
     *
     * @param plantId: id of plant species for the submission
     * @param latitude: latitude of the submission's location
     * @param longitude: longitude of the submission's location
     * @param postedOn: Amount of time elapsed in milliseconds between this submission being posted
     * and midnight, January 1, 1970 UTC
     * @param postedBy: Username of the poster of the submission
     * @param image: Bitmap of photo for the submission
     * @param listener: Listener to be invoked upon success. Will be supplied an integer, the
     * id of the newly posted submission
     * @param errorListener: Listener to be invoked upon failure. Will be supplied a VollyError
     * object
     */
    fun postSubmission(
        plantId: Int, latitude: Float, longitude: Float, postedOn: Long,
        postedBy: String, image: Bitmap, listener: Response.Listener<Int>,
        errorListener: Response.ErrorListener)

    fun reportSubmission(
        submissionId: Int, listener: Response.Listener<Int>, errorListener: Response.ErrorListener
    )

    /**
     * Sends a request to get the image for a submission
     *
     * @param submissionId: id of the submission whose image will be requested
     * @param listener: Listener to be invoked upon success. Will be supplied a Bitmap of the
     * image
     * @param errorListener: Listener to be invoked upon failure. Will be supplied a VollyError
     * object
     */
    fun getImage(submissionId: Int, scaleType: ImageView.ScaleType, listener: Response.Listener<Bitmap>,
                 errorListener: Response.ErrorListener)
}

