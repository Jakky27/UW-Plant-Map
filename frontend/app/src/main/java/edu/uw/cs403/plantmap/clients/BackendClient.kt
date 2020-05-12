package edu.uw.cs403.plantmap.clients

import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import edu.uw.cs403.plantmap.BadResponseException
import edu.uw.cs403.plantmap.models.Plant
import edu.uw.cs403.plantmap.models.Submission
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.message.BasicNameValuePair

class BackendClient(private val client: HttpClient): PlantMapClient {
    private val MAPPER = jacksonObjectMapper()
    private val BASE_URL = "https://plantmap.herokuapp.com/v/"
    private val PLANT = "plant"
    private val SUBMISSION = "submissions"
    private val IMAGE = "image"

    override fun getSubmissions(): List<Submission> {
        val get = HttpGet(BASE_URL + SUBMISSION)

        val response = client.execute(get)

        if (response.statusLine.statusCode != 200) {
            throw BadResponseException("Bad response " + response.statusLine.statusCode, response)
        } else {
            return MAPPER.readValue(response.entity.content)
        }
    }

    override fun getPlant(plantId: Int): Plant {
        val get = HttpGet(BASE_URL + PLANT)

        val response = client.execute(get)

        if (response.statusLine.statusCode != 200) {
            throw BadResponseException("Bad response " + response.statusLine.statusCode, response)
        } else {
            return MAPPER.readValue(response.entity.content)
        }
    }

    override fun postPlant(name: String, description: String): Int {
        val post = HttpPost(BASE_URL + PLANT)

        val params = ArrayList<NameValuePair>()
        params.add(BasicNameValuePair("name", name))
        params.add(BasicNameValuePair("description", description))

        post.entity = UrlEncodedFormEntity(params)

        val response = client.execute(post)

        if (response.statusLine.statusCode != 200) {
            throw BadResponseException("Bad response " + response.statusLine.statusCode, response)
        } else {
            return MAPPER.readValue<ObjectNode>(response.entity.content).get("plant_id").asInt()
        }
    }

    override fun getSubmission(submissionId: Int): Submission {
        val get = HttpGet("$BASE_URL$SUBMISSION?id=$submissionId")

        val response = client.execute(get)

        if (response.statusLine.statusCode != 200) {
            throw BadResponseException("Bad response " + response.statusLine.statusCode, response)
        } else {
            return MAPPER.readValue(response.entity.content)
        }
    }

    override fun postSubmission(plantId: Int, latitude: Float, longitude: Float, postedOn: Long,
                       postedBy: String): Int {
        val post = HttpPost(BASE_URL + SUBMISSION)

        val params = ArrayList<NameValuePair>()
        params.add(BasicNameValuePair("plant_id", plantId.toString()))
        params.add(BasicNameValuePair("latitude", latitude.toString()))
        params.add(BasicNameValuePair("longitude", longitude.toString()))
        params.add(BasicNameValuePair("posted_on", postedOn.toString()))
        params.add(BasicNameValuePair("posted_by", postedBy))

        post.entity = UrlEncodedFormEntity(params)

        val response = client.execute(post)

        if (response.statusLine.statusCode != 200) {
            throw BadResponseException("Bad response " + response.statusLine.statusCode, response)
        } else {
            return MAPPER.readValue<ObjectNode>(response.entity.content).get("sub_id").asInt()
        }
    }

    override fun getImage(submissionId: Int): ByteArray {
        val get = HttpGet("$BASE_URL$IMAGE?id=$submissionId")

        val response = client.execute(get)

        if (response.statusLine.statusCode != 200) {
            throw BadResponseException("Bad response " + response.statusLine.statusCode, response)
        } else {
            return MAPPER.readValue(response.entity.content)
        }
    }

    override fun postImage(submissionId: Int) {
        val post = HttpPost("$BASE_URL$IMAGE?id=$submissionId")

        val response = client.execute(post)

        if (response.statusLine.statusCode != 200) {
            throw BadResponseException("Bad response " + response.statusLine.statusCode, response)
        }
    }
}
