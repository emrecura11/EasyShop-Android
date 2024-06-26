package com.emrecura.easyshop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emrecura.easyshop.R
import com.emrecura.easyshop.models.PostElement
import com.emrecura.easyshop.models.User

class PostAdapter(private val posts: List<PostElement>, private val user: User) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

        inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val user_image: ImageView = itemView.findViewById(R.id.user_image)
            val title: TextView = itemView.findViewById(R.id.post_title)
            val like_text: TextView = itemView.findViewById(R.id.like_text)
            val dislike_text: TextView = itemView.findViewById(R.id.dislike_text)
            val username: TextView = itemView.findViewById(R.id.post_username)
            val post_description : TextView  =itemView.findViewById(R.id.post_description)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_row, parent, false)
            return PostViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            val post = posts[position]
            holder.title.text = post.title
            holder.like_text.text = post.reactions.likes.toString()
            holder.dislike_text.text = post.reactions.dislikes.toString()
            holder.username.text = user.username
            holder.post_description.text = post.body
            Glide.with(holder.itemView.context).load(user.image).into(holder.user_image)
        }

        override fun getItemCount() = posts.size
    }

