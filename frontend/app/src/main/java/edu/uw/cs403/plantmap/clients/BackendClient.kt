package edu.uw.cs403.plantmap.clients

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import edu.uw.cs403.plantmap.models.Plant
import edu.uw.cs403.plantmap.models.Submission
import org.json.JSONObject

/**
 * Implementation of UWPlantMapClient that sends requests to our backend API. Implemented as a
 * singleton.
 */
class BackendClient constructor(private var requestQueue: RequestQueueSingleton): UWPlantMapClient {
    companion object {
        @Volatile
        private var INSTANCE: BackendClient? = null
        fun getInstance(requestQueue: RequestQueueSingleton) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BackendClient(requestQueue).also {
                    INSTANCE = it
                }
            }
    }

    private val MAPPER = ObjectMapper()

    // API paths
    private val API_BASE_URL = "https://plantmap.herokuapp.com/v1/"
    private val API_SUBMISSION_PATH = API_BASE_URL + "submission"
    private val API_PLANT_PATH = API_BASE_URL + "plant"

    override fun getSubmissions(listener: Response.Listener<List<Submission>>,
                                errorListener: Response.ErrorListener) {
        val request =
            StringRequest(
            Request.Method.GET, API_SUBMISSION_PATH,
                Response.Listener { response ->
                    val submissions: List<Submission> = MAPPER.readValue(response)
                    listener.onResponse(submissions)
                },
            errorListener)

        requestQueue.addToRequestQueue(request)
    }

    override fun getPlant(plantId: Int): Plant {
        TODO("Not yet implemented")
    }

    override fun postPlant(name: String, description: String, listener: Response.Listener<Int>,
                           errorListener: Response.ErrorListener) {
        val entity = JSONObject()
        entity.put("name", name)
        entity.put("description", description)

        val request = object:
            StringRequest(Method.POST, API_PLANT_PATH,
                Response.Listener { response ->
                    listener.onResponse(response.toInt())
                },
                errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
            override fun getBody(): ByteArray {
                return entity.toString().toByteArray()
            }
        }

        requestQueue.addToRequestQueue(request)
    }

    override fun getSubmission(submissionId: Int): Submission {
        TODO("Not yet implemented")
    }

    override fun postSubmission(
        plantId: Int,
        latitude: Float,
        longitude: Float,
        postedOn: Long,
        postedBy: String,
        listener: Response.Listener<Int>,
        errorListener: Response.ErrorListener
    ) {
        val entity = JSONObject()
        entity.put("plant_id", plantId)
        entity.put("latitude", latitude)
        entity.put("longitude", longitude)
        entity.put("posted_on", postedOn)
        entity.put("posted_by", postedBy)

        val request = object:
            StringRequest(Method.POST, API_SUBMISSION_PATH,
                Response.Listener { response ->
                    listener.onResponse(response.toInt())
                },
                errorListener)
        {
            override fun getHeaders(): MutableMap<String, String>{
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }

            override fun getBody(): ByteArray {
                return entity.toString().toByteArray()
            }
        }

        requestQueue.addToRequestQueue(request)
    }

    override fun getImage(submissionId: Int): ByteArray {
        TODO("Not yet implemented")
    }
}