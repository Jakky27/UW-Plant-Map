package edu.uw.cs403.plantmap

import org.apache.http.HttpResponse
import java.io.IOException

class BadResponseException(message: String, val response: HttpResponse): IOException(message)