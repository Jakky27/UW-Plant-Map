package edu.uw.cs403.plantmap.clients

import com.android.volley.Response
import edu.uw.cs403.plantmap.models.Plant
import edu.uw.cs403.plantmap.models.Submission

interface UWPlantMapClient {
    fun getSubmissions(listener: Response.Listener<List<Submission>>,
                       errorListener: Response.ErrorListener)

    fun getPlant(plantId: Int): Plant

    fun postPlant(name: String, description: String, listener: Response.Listener<Int>,
                  errorListener: Response.ErrorListener)

    fun getSubmission(submissionId: Int): Submission

    fun postSubmission(
        plantId: Int, latitude: Float, longitude: Float, postedOn: Long,
        postedBy: String, listener: Response.Listener<Int>, errorListener: Response.ErrorListener)

    fun getImage(submissionId: Int): ByteArray
}

