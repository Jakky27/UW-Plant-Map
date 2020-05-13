package edu.uw.cs403.plantmap.clients

import edu.uw.cs403.plantmap.models.Submission

class AppClient(private val plantMapClient: PlantMapClient) {

    fun registerPlant(name: String, description: String): Int {
        return plantMapClient.postPlant(name, description)
    }

    fun postSubmission(plantId: Int, latitude: Float, longitude: Float, postedOn: Long,
                       postedBy: String): Int {
        return plantMapClient.postSubmission(plantId, latitude, longitude, postedOn, postedBy)
    }

    fun getSubmissions(): List<Submission> {
        return plantMapClient.getSubmissions()
    }
}