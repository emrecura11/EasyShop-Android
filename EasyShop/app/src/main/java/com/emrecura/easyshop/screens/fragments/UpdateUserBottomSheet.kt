package com.emrecura.easyshop.screens.fragments

import android.widget.Button
import com.emrecura.easyshop.R
import com.emrecura.easyshop.configs.ApiClient
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.emrecura.easyshop.models.UserUpdateRequest
import com.emrecura.easyshop.models.User
import com.emrecura.easyshop.services.UserService
import com.emrecura.easyshop.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserBottomSheet : BottomSheetDialogFragment() {

    private lateinit var userService: UserService
    private lateinit var sessionManager: SessionManager
    private lateinit var user: User
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var updateButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_update_user, container, false)

        userService = ApiClient.getClient().create(UserService::class.java)
        sessionManager = SessionManager(requireContext())

        firstNameEditText = view.findViewById(R.id.firstNameEditText)
        lastNameEditText = view.findViewById(R.id.lastNameEditText)
        usernameEditText = view.findViewById(R.id.usernameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        updateButton = view.findViewById(R.id.updateButton)
        user = User(0,"","","","","","","")
        val userId = sessionManager.fetchUserId()

        fetchUserData()
        setText()

        updateButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()

            val userUpdateRequest = UserUpdateRequest(
                firstName = firstName,
                lastName = lastName,
                username = username,
                email = email
            )

            updateUser(userId, userUpdateRequest)
        }

        return view
    }

    private fun updateUser(userId: Long, userUpdateRequest: UserUpdateRequest) {
        userService.updateUser(userId, userUpdateRequest).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val updatedUser = response.body()
                    Toast.makeText(requireContext(), "User updated: $updatedUser", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Failed to update user: ${response.errorBody()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setText() {
        firstNameEditText.setText(user.firstName)
        lastNameEditText.setText(user.lastName)
        emailEditText.setText(user.email)
        usernameEditText.setText(user.username)

    }
    private fun fetchUserData() {
        val token = sessionManager.fetchAuthToken()
        val userService = ApiClient.getClient().create(UserService::class.java)
        val call = userService.getUserInfo(token = "Bearer $token")
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    user = response.body()!!
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
            }
        })

    }
}
