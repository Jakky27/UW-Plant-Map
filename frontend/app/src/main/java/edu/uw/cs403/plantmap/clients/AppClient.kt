package edu.uw.cs403.plantmap.clients

class AppClient(private val plantMapClient: PlantMapClient) {

    fun registerPlant(name: String, description: String): Int {
        return plantMapClient.postPlant(name, description)
    }

    fun postSubmission(plantId: Int, latitude: Float, longitude: Float, postedOn: Long,
                       postedBy: String): Int {
        return plantMapClient.postSubmission(plantId, latitude, longitude, postedOn, postedBy)
    }
}