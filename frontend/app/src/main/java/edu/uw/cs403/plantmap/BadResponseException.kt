package edu.uw.cs403.plantmap

import org.apache.http.HttpResponse

class BadResponseException(message: String, val response: HttpResponse): Exception(message)