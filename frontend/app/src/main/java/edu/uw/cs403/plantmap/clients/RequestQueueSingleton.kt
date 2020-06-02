package edu.uw.cs403.plantmap.clients

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Singleton request queue, supply with application context to get correct singleton.
 * Copied from android docs.
 */
class RequestQueueSingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: RequestQueueSingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RequestQueueSingleton(context).also {
                    INSTANCE = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    /**
     * Adds the supplied request to the queue of requests to be executed
     *
     * @param req: Request to be added to the queue
     */
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}