package com.emrecura.easyshop.screens.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.emrecura.easyshop.configs.ApiClient
import com.emrecura.easyshop.models.User
import com.emrecura.easyshop.models.UserLogin
import com.emrecura.easyshop.services.UserService
import com.emrecura.easyshop.utils.SessionManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this.applicationContext)

        setContent {
            LoginPage()
        }
    }

    @Composable
    fun LoginPage() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val context = LocalContext.current
        val systemUiController = rememberSystemUiController()



        systemUiController.setStatusBarColor(
            color = Color.Transparent,
        )

        com.emrecura.easyshop.screens.compose.LoginPage(
            onUsernameChange = { username = it },
            onPasswordChange = { password = it },
            onLoginClick = {
                loginUser(context, username, password)
            }
        )
    }

    private fun loginUser(context: Context, username: String, password: String) {
        val userService = ApiClient.getClient().create(UserService::class.java)
        val call = userService.userLogin(UserLogin(username, password))

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()

                    saveSharedPref(user!!)

                    Toast.makeText(context, "Welcome ${user.username}", Toast.LENGTH_SHORT).show()
                    val mainActivityIntent = Intent(context, MainActivity::class.java)
                    context.startActivity(mainActivityIntent)
                    finish()
                } else {
                    Toast.makeText(context, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun saveSharedPref(user: User){
        val token = user.token
        val userId = user.id
        sessionManager.saveAuthToken(token, System.currentTimeMillis() + 3600000) // 1 hour expiration
        sessionManager.saveUserId(userId)

    }

}
