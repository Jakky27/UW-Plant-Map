package edu.uw.cs403.plantmap.clients

interface PlantMapClient {

    // At the moment, most of these don't have the correct return type

    fun getSubmissions()

    fun getPlant(plantId: Int)

    fun postPlant(name: String, description: String): Int

    fun getSubmission(submissionId: Int)

    fun postSubmission(plantId: Int, latitude: Float, longitude: Float, postedOn: Long,
                       postedBy: String): Int

    fun getImage(submissionId: Int)

    fun postImage(submissionId: Int)
}