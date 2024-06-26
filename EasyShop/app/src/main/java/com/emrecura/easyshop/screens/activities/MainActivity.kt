package com.emrecura.easyshop.screens.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.emrecura.easyshop.R
import com.emrecura.easyshop.databinding.ActivityMainBinding
import com.emrecura.easyshop.screens.fragments.CategoriesFragment
import com.emrecura.easyshop.screens.fragments.FavoriteProductFragment
import com.emrecura.easyshop.screens.fragments.HomeFragment
import com.emrecura.easyshop.screens.fragments.OrdersFragment
import com.emrecura.easyshop.screens.fragments.ProfileFragment
import com.emrecura.easyshop.screens.fragments.SearchFragment
import com.emrecura.easyshop.screens.fragments.UpdateUserBottomSheet
import com.emrecura.easyshop.utils.SessionManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the user is logged in
        sessionManager = SessionManager(this.applicationContext)
        isLogin()



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDrawer()
        val fragment = HomeFragment()
        addFragment(fragment)
        setToolbarTitle("Home")

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // Firebase Remote Config ayarları
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        fetchRemoteConfig()
    }

    private fun isLogin(){
        if (sessionManager.isTokenValid()) {
            replaceFragment(HomeFragment())
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, 0, 0)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setToolbarTitle(title: String) {
        binding.toolbar.title = title
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                com.google.android.material.R.anim.design_bottom_sheet_slide_in,
                com.google.android.material.R.anim.design_bottom_sheet_slide_out
            )
            .replace(R.id.frameLayout, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)

        when (item.itemId) {
            R.id.home_drawer -> {
                replaceFragment(HomeFragment())
                setToolbarTitle("Home")
            }
            R.id.categories_drawer -> {
                replaceFragment(CategoriesFragment())
                setToolbarTitle("Categories")
            }
            R.id.search_drawer -> {
                replaceFragment(SearchFragment())
                setToolbarTitle("Search")
            }
            R.id.favorites_drawer -> {
                replaceFragment(FavoriteProductFragment())
                setToolbarTitle("Favorites")
            }
            R.id.orders_drawer -> {
                replaceFragment(OrdersFragment())
                setToolbarTitle("Orders")
            }
            R.id.profile_drawer -> {
                replaceFragment(ProfileFragment())
                setToolbarTitle("Profile")
            }
            R.id.update_profile -> {
                val updateUserBottomSheet = UpdateUserBottomSheet()
                updateUserBottomSheet.show(supportFragmentManager, "UpdateUserBottomSheet")
            }
            R.id.logout_drawer -> logout()
        }
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }

    private fun logout() {
        sessionManager.clearAuthToken()

        val loginActivityIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginActivityIntent)
        finish()
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    private fun fetchRemoteConfig() {
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("RemoteConfig", "Config params updated: $updated")
                    applyRemoteConfig()
                } else {
                    Log.e("RemoteConfig", "Fetch failed")
                }
            }

        firebaseRemoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.e("onUpdate", "Updated keys: " + configUpdate.updatedKeys)

                if (configUpdate.updatedKeys.contains("background_color")) {
                    firebaseRemoteConfig.activate().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val color = FirebaseRemoteConfig.getInstance().getString("background_color")
                            Log.d("Pull Color", color)
                        }
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w("onError", "Config update error with code: " + error.code, error)
            }
        })

    }

    private fun applyRemoteConfig() {
        val backgroundColor = firebaseRemoteConfig.getString("background_color")
        window.decorView.setBackgroundColor(Color.parseColor(backgroundColor))
    }
}
