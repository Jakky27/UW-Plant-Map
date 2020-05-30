package edu.uw.cs403.plantmap.ui.feed

import android.content.Context
import android.content.Intent
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
import com.android.volley.Response
import com.squareup.picasso.Picasso
import edu.uw.cs403.plantmap.R
import edu.uw.cs403.plantmap.clients.BackendClient
import edu.uw.cs403.plantmap.clients.RequestQueueSingleton
import edu.uw.cs403.plantmap.clients.UWPlantMapClient
import edu.uw.cs403.plantmap.models.Plant
import edu.uw.cs403.plantmap.models.Submission
import edu.uw.cs403.plantmap.ui.ViewSubmissionActivity
import java.util.*

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var postsView: RecyclerView

    private lateinit var posts: ArrayList<Post>

    private lateinit var client: UWPlantMapClient

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
        client = BackendClient.getInstance(
            RequestQueueSingleton.getInstance(this.context!!.applicationContext)
        )
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
        client.getSubmissions(
            Response.Listener<List<Submission>> { submissions ->
                for (submission in submissions) {
                    val plantId = submission.plant_id!!
                    val subId = submission.post_id!!
                    Log.d("DEBUG", "REQUEST");

                    client.getPlant(plantId, Response.Listener<Plant> {plant ->

                        posts.add(Post(submission, plant))

                        if (posts.size == submissions.size) {
                            posts.sortBy{ it.submission.post_date }
                            postsView.adapter!!.notifyDataSetChanged()
                        }
                    },
                    Response.ErrorListener {
                        Log.d("DEBUG", "Description Handle error")
                    })
                }
            },
            Response.ErrorListener {
                Log.d("DEBUG", "Submission Handle error")
            }
        )
    }
}



data class Post(val submission: Submission, val plant: Plant)

private class PostAdapter(val posts: ArrayList<Post>, val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private lateinit var client: UWPlantMapClient

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        client = BackendClient.getInstance(
            RequestQueueSingleton.getInstance(this.context.applicationContext)
        )
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.live_feed_post, parent, false))
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.plantName.text = posts[position].plant.name

        holder.description.text = posts[position].plant.description

        Picasso.get()
            .load("https://plantmap.herokuapp.com/v1/image/" + posts[position].submission.post_id)
            .into(holder.postPhoto)

        holder.postDate.text = String.format("%tc", Date(posts[position].submission.post_date!!))

        holder.itemView.setOnClickListener { _ ->
            val intent = Intent(context, ViewSubmissionActivity::class.java)
            intent.putExtra("submissionId", posts[position].submission.post_id)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantName: TextView = itemView.findViewById((R.id.postTitle))
        val description: TextView = itemView.findViewById(R.id.postDescription)
        val postPhoto: ImageView = itemView.findViewById(R.id.postImage)
        val postDate: TextView = itemView.findViewById(R.id.dateText)
    }
}