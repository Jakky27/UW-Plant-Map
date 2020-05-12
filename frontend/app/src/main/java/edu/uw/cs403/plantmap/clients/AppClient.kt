package edu.uw.cs403.plantmap.clients

class AppClient(val plantMapClient: PlantMapClient) {

    fun registerPlant(name: String, description: String): Int {
        return plantMapClient.postPlant(name, description)
    }
}