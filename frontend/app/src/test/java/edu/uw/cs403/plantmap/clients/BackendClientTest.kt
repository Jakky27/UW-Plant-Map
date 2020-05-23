package edu.uw.cs403.plantmap.clients

import android.graphics.Bitmap
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import edu.uw.cs403.plantmap.models.Submission
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify

class BackendClientTest {
    // API paths
    private val API_BASE_URL = "https://plantmap.herokuapp.com/v1/"
    private val API_SUBMISSION_PATH = API_BASE_URL + "submission"
    private val API_PLANT_PATH = API_BASE_URL + "plant"

    private val MAPPER = ObjectMapper()

    lateinit var backendClient: BackendClient
    lateinit var mockRequestQueueSingleton: RequestQueueSingleton
    lateinit var mockErrorListener: Response.ErrorListener
    lateinit var requestCaptor: KArgumentCaptor<StringRequest>

    @Before
    fun init() {
        mockErrorListener = mock()

        mockRequestQueueSingleton = mock()
        backendClient = BackendClient(mockRequestQueueSingleton)

        requestCaptor = argumentCaptor()
    }

    @Test
    fun testGetSubmissions() {
        val listener = mock<Response.Listener<List<Submission>>>()

        backendClient.getSubmissions(listener, mockErrorListener)

        verify(mockRequestQueueSingleton).addToRequestQueue(requestCaptor.capture())

        val r = requestCaptor.firstValue

        assert(r.method == Request.Method.GET)
        assert(r.url == API_SUBMISSION_PATH)
        assert(r.errorListener == mockErrorListener)
    }

    @Test
    fun testPostPlant() {
        val name = "testName"
        val desc = "testDescription"
        val listener = mock<Response.Listener<Int>>()

        backendClient.postPlant(name, desc, listener, mockErrorListener)

        verify(mockRequestQueueSingleton).addToRequestQueue(requestCaptor.capture())

        val r = requestCaptor.firstValue

        assert(r.method == Request.Method.POST)
        assert(r.url == API_PLANT_PATH)
        assert(r.errorListener == mockErrorListener)

        // Check body is correct

        val body = MAPPER.readTree(r.body)

        assert(body.get("name").asText() == name)
        assert(body.get("description").asText() == desc)
        assert(body.size() == 2)
    }

    @Test
    fun testPostSubmission() {
        val plantId = 1
        val latitude = 2f
        val longitude = 3f
        val postedOn = 5L
        val postedBy = "Test User"
        val image = mock<Bitmap>()

        val listener = mock<Response.Listener<Int>>()

        backendClient.postSubmission(plantId, latitude, longitude, postedOn, postedBy, image, listener,
            mockErrorListener)

        verify(mockRequestQueueSingleton).addToRequestQueue(requestCaptor.capture())

        val r = requestCaptor.firstValue

        assert(r.method == Request.Method.POST)
        assert(r.url == API_SUBMISSION_PATH)
        assert(r.errorListener == mockErrorListener)

        // Check body is correct
        val body = MAPPER.readTree(r.body)

        assert(body.get("plant_id").asInt() == plantId)
        assert(body.get("latitude").asDouble().toFloat() == latitude)
        assert(body.get("longitude").asDouble().toFloat() == longitude)
        assert(body.get("posted_on").asLong() == postedOn)
        assert(body.get("posted_by").asText() == postedBy)

        assert(body.size() == 5)
    }
}