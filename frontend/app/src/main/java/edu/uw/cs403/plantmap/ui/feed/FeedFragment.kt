package edu.uw.cs403.plantmap.ui.feed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.uw.cs403.plantmap.R
import java.util.*

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var postsView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel =
            ViewModelProviders.of(this).get(FeedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_feed, container, false)

        postsView = root.findViewById(R.id.postsView)
        val posts: ArrayList<Post> = ArrayList()
        posts.add(Post("https://i.imgur.com/NoXtk.jpg", "This is the description of the flower", Date(10L)))
        posts.add(Post("https://i.imgur.com/NoXtk.jpg", "This is the description of the flower 2", Date(10L)))
        posts.add(Post("https://i.imgur.com/NoXtk.jpg", "This is the description of the flower 3", Date(10L)))

        postsView.layoutManager = LinearLayoutManager(this.context)
        postsView.adapter = PostAdapter(posts, this.context!!)

        return root
    }
}

private fun updatePostsList() {
    // TODO communicate with server here

    // TODO update flowers
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
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.postDescription)
        val postPhoto: ImageView = itemView.findViewById(R.id.postImage)
    }
}