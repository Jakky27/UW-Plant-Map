package edu.uw.cs403.plantmap.clients

import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import edu.uw.cs403.plantmap.BadResponseException
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.message.BasicNameValuePair

class BackendClient(val client: HttpClient) : PlantMapClient {
    val MAPPER = jacksonObjectMapper()
    val BASE_URL = "https://plantmap.herokuapp.com/v/"
    val PLANT = "plant"
    val SUBMISSION = "plant"


    override fun getSubmissions() {
        //TODO: implement
    }

    override fun getPlant(plantId: Int) {
        //TODO: implement
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

    override fun getSubmission(submissionId: Int) {
        //TODO: implement
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

    override fun getImage(submissionId: Int) {
        //TODO: implement
    }

    override fun postImage(submissionId: Int) {
        //TODO: implement
    }
}
