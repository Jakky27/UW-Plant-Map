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
import com.squareup.picasso.Picasso
import edu.uw.cs403.plantmap.R
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.math.log

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
        queue = Volley.newRequestQueue(this.context)
        updatePostsList()

        postsView.layoutManager = LinearLayoutManager(this.context)
        postsView.adapter = PostAdapter(posts, this.context!!)

        return root
    }


    /**
     * Populates the live feed with the most recent posts from the app's server
     *
     */
    private fun updatePostsList() {
        // TODO communicate with server here

        val url = "https://plantmap.herokuapp.com/v1/submission"
        val descriptionURL = "https://plantmap.herokuapp.com/v1/plant/"
        val imgURL = "https://plantmap.herokuapp.com/v1/image/"


        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // TODO: Update flowers here

                Log.d("DEBUG", response.length().toString())

                for(x in 0 until response.length()) {
                    val res = response[x] as JSONObject

                    val subID = res.getString("post_id")
                    val date = res.getLong("post_date")

                    //Log.d("DEBUG", "image url: $imgURL$subID")
                    //Log.d("DEBUG", "description url: $descriptionURL$subID")

                    val descriptionObjectRequest = JsonObjectRequest(Request.Method.GET, descriptionURL + subID, null,
                        Response.Listener { response ->

                            val plantName = response.getString("name")
                            val plantDescription = response.getString("description")

                            //Log.d("DEBUG", "Plant Name: $plantName")

                            posts.add(Post(plantName, imgURL + subID, plantDescription, Date(date)))
                            postsView.adapter!!.notifyDataSetChanged()

                        }, Response.ErrorListener { error ->
                            Log.d("DEBUG", "Description Handle error")
                            Log.d("DEBUG", error.message)
                        }
                    )
                    queue.add(descriptionObjectRequest) // automatically requests
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



private data class Post(val plantName: String, val photo: String, val description: String, val date: Date)

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