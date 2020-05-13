package edu.uw.cs403.plantmap

import android.app.Application
import edu.uw.cs403.plantmap.clients.AppClient
import edu.uw.cs403.plantmap.clients.BackendClient
import org.apache.http.impl.client.HttpClientBuilder

class UWPlantMapApplication: Application() {
    val appClient: AppClient = AppClient(BackendClient(HttpClientBuilder.create().build()))
}