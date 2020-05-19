package edu.uw.cs403.plantmap.clients

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

    fun getPlant(plantId: Int): Plant

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

    fun getSubmission(submissionId: Int): Submission

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
     * @param listener: Listener to be invoked upon success. Will be supplied an integer, the
     * id of the newly posted submission
     * @param errorListener: Listener to be invoked upon failure. Will be supplied a VollyError
     * object
     */
    fun postSubmission(
        plantId: Int, latitude: Float, longitude: Float, postedOn: Long,
        postedBy: String, listener: Response.Listener<Int>, errorListener: Response.ErrorListener)

    fun getImage(submissionId: Int): ByteArray
}

