package edu.uw.cs403.plantmap.clients

import edu.uw.cs403.plantmap.models.Plant
import edu.uw.cs403.plantmap.models.Submission

interface PlantMapClient {
    fun getSubmissions(): List<Submission>

    fun getPlant(plantId: Int): Plant

    fun postPlant(name: String, description: String): Int

    fun getSubmission(submissionId: Int): Submission

    fun postSubmission(plantId: Int, latitude: Float, longitude: Float, postedOn: Long,
                       postedBy: String): Int

    fun getImage(submissionId: Int): ByteArray

    fun postImage(submissionId: Int)
}