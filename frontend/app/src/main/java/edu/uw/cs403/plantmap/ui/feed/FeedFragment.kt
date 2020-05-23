package edu.uw.cs403.plantmap.ui.feed

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import edu.uw.cs403.plantmap.R
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.math.log
import edu.uw.cs403.plantmap.clients.BackendClient
import edu.uw.cs403.plantmap.clients.RequestQueueSingleton
import edu.uw.cs403.plantmap.clients.UWPlantMapClient

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var postsView: RecyclerView
    private lateinit var queue: RequestQueue

    private lateinit var posts: ArrayList<Post>

    private lateinit var client: UWPlantMapClient

    private var postCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel =
            ViewModelProviders.of(this).get(FeedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_feed, container, false)

        postsView = root.findViewById(R.id.postsView)
        posts = ArrayList()
        queue = Volley.newRequestQueue(this.context)
        updatePostsList()

        client = BackendClient.getInstance(
            RequestQueueSingleton.getInstance(this.context!!.applicationContext)
        )

        postsView.layoutManager = LinearLayoutManager(this.context)
        postsView.adapter = PostAdapter(posts, this.context!!)

        return root
    }



    /**
     * Populates the live feed with the most recent posts from the app's server
     *
     */
    private fun updatePostsList() {
        val url = "https://plantmap.herokuapp.com/v1/submission"
        val descriptionURL = "https://plantmap.herokuapp.com/v1/plant/"
        val imgURL = "https://plantmap.herokuapp.com/v1/image/"

        postCount = 0

        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->

                Log.d("DEBUG", response.length().toString())

                for(x in 0 until response.length()) {
                    val res = response[x] as JSONObject

                    val subID = res.getString("post_id")
                    val date = res.getLong("post_date")

                    val length = response.length()

                    val descriptionObjectRequest = JsonObjectRequest(Request.Method.GET, descriptionURL + subID, null,
                        Response.Listener { response ->

                            val plantName = response.getString("name")
                            val plantDescription = response.getString("description")

                            posts.add(Post(plantName, imgURL + subID, plantDescription, Date(date)))
                            posts.sortByDescending { it.date }

                            postCount++

                            if (postCount == length) {
                                postsView.adapter!!.notifyDataSetChanged()
                            }


                        }, Response.ErrorListener { error ->
                            Log.d("DEBUG", "Description Handle error")
                            //Log.d("DEBUG", error.message)
                        }
                    )
                    queue.add(descriptionObjectRequest)
                }
            },
            Response.ErrorListener { error ->
                Log.d("DEBUG", "Submission Handle error")
                //Log.d("DEBUG", error.message)
            }
        )
        queue.add(jsonObjectRequest) // automatically requests
    }
}



public data class Post(val plantName: String, val photo: String, val description: String, val date: Date)

private class PostAdapter(val posts: ArrayList<Post>, val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.live_feed_post, parent, false))
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.plantName.text = posts[position].plantName

        holder.description.text = posts[position].description

        Picasso.get()
            .load(posts[position].photo)
            .into(holder.postPhoto)


        holder.postDate.text = String.format("%tc", posts[position].date);
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantName: TextView = itemView.findViewById((R.id.postTitle))
        val description: TextView = itemView.findViewById(R.id.postDescription)
        val postPhoto: ImageView = itemView.findViewById(R.id.postImage)
        val postDate: TextView = itemView.findViewById(R.id.dateText)
    }
}