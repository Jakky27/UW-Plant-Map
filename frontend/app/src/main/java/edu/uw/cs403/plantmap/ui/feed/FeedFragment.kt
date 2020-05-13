package edu.uw.cs403.plantmap.ui.feed

import android.content.Context
import android.database.DataSetObserver
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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import edu.uw.cs403.plantmap.R
import org.json.JSONObject
import java.util.*
import kotlin.math.log

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var postsView: RecyclerView
    private lateinit var queue: RequestQueue

    private lateinit var posts: ArrayList<Post>

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
        //posts.add(Post("https://i.imgur.com/NoXtk.jpg", "This is the description of the flower", Date(10L)))

        queue = Volley.newRequestQueue(this.context)
        updatePostsList()


        postsView.layoutManager = LinearLayoutManager(this.context)
        postsView.adapter = PostAdapter(posts, this.context!!)

        return root
    }

    private var TEMP_IMAGES_URL = arrayOf(
        "https://i.imgur.com/NoXtk.jpg",
        "https://i.imgur.com/QRn0O0E.jpg",
        "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/close-up-of-flower-blooming-outdoors-royalty-free-image-739387273-1544039749.jpg",
        "https://i.imgur.com/s9f9abx.jpg",
        "https://i.imgur.com/QfDGakI.jpg",
        "https://i.imgur.com/R2l26fI.jpg",
        "https://i.imgur.com/WbhUbh7.png",
        "https://i.imgur.com/LvxYIvQb.jpg",
        "https://i.imgur.com/knGlNmyb.jpg",
        "https://i.imgur.com/H18lXnKb.jpg"
    )

    private fun updatePostsList() {
        // TODO communicate with server here

        val url = "https://plantmap.herokuapp.com/v1/submission"
        val imgURL = "https://plantmap.herokuapp.com/v1/submission?id="

        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // TODO: Update flowers here

                Log.d("DEBUG", response.length().toString())

                for(x in 0 until response.length()) {
                    val res = response[x] as JSONObject

                    val subID = res.getString("post_id")
                    val date = res.getLong("post_date")

                    //posts.add(Post(imgURL + subID, "This is the description of the flower", Date(10L)))
                    posts.add(Post(TEMP_IMAGES_URL[x], "flower description", Date(date) ))
                    postsView.adapter!!.notifyDataSetChanged()
                }

                //val postRequest = ImageRequest(img_url + sub_id,
                //    Response.Listener<Bitmap> { response ->

                //    }, 0, 0, null,
                //    Response.ErrorListener { error ->
                //        Log.d("DEBUG", "Image handle error")
                //    }
                //)

            },
            Response.ErrorListener { error ->
                Log.d("DEBUG", "Handle error")
                //Log.d("DEBUG", error.message)
            }
        )
        queue.add(jsonObjectRequest) // automatically requests

        // TODO: REMOVE
        val websiteRequest = StringRequest(Request.Method.GET, "https://plantmap.herokuapp.com/",
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Log.d("DEBUG", response.toString())
            },
            Response.ErrorListener { error ->
                Log.d("DEBUG", "Test Handle error")
            })
        queue.add(websiteRequest)

    }
}



private data class Post(val photo: String, val description: String, val date: Date)

private class PostAdapter(val posts: ArrayList<Post>, val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.live_feed_post, parent, false))
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.description.text = posts[position].description
        Picasso.get().load(posts[position].photo).into(holder.postPhoto)
        holder.postDate.text = String.format("%tc", posts[position].date);
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.postDescription)
        val postPhoto: ImageView = itemView.findViewById(R.id.postImage)
        val postDate: TextView = itemView.findViewById(R.id.dateText)
    }
}