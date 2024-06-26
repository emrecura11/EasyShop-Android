package com.emrecura.easyshop.screens.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emrecura.easyshop.R
import com.emrecura.easyshop.adapters.PostAdapter
import com.emrecura.easyshop.configs.ApiClient
import com.emrecura.easyshop.models.Post
import com.emrecura.easyshop.models.PostElement
import com.emrecura.easyshop.models.User
import com.emrecura.easyshop.services.UserService
import com.emrecura.easyshop.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var profileImage : ImageView
    private lateinit var usernameText : TextView
    private lateinit var mailText : TextView
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        sessionManager = SessionManager(view.context)
        postRecyclerView = view.findViewById(R.id.post_recyclerview)
        postRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        profileImage =view.findViewById(R.id.profile_image)
        usernameText = view.findViewById(R.id.username_text)
        mailText = view.findViewById(R.id.mail_text)


        fetchUserData(view.context)
        return view
    }


    private fun fetchUserData(context: Context) {
        val token = sessionManager.fetchAuthToken()
        val userService = ApiClient.getClient().create(UserService::class.java)
        val call = userService.getUserInfo(token = "Bearer $token")

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        user = response.body()!!
                        setView(user, context)
                        fetchPosts()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(context, "Failed to load orders", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun fetchPosts() {
        val userService = ApiClient.getClient().create(UserService::class.java)
        val userId = sessionManager.fetchUserId()
        val call = userService.getPosts(userId)

        call.enqueue(object : Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful){
                    setAdapter(response.body()!!.posts)
                }
            }

            override fun onFailure(p0: Call<Post>, p1: Throwable) {

            }

        })
    }

    private fun setAdapter(posts : List<PostElement>){
        postRecyclerView.adapter = PostAdapter(posts, user)
    }
    private fun setView(user: User, context: Context){
        Glide.with(context).load(user.image).into(profileImage)
        usernameText.text = user.username
        mailText.text = user.email
    }

}