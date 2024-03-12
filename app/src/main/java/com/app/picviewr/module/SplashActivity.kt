package com.app.picviewr.module

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.picviewr.util.NetworkManager
import com.app.picviewr.util.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var handler: Handler

    @Inject
    lateinit var sharedPref: SharedPrefManager

    @Inject
    lateinit var authService: FirebaseAuth

    private var keepSplashOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if(NetworkManager(this).isNetworkAvailable()) {
                val user =  authService.currentUser
                if(user != null && sharedPref.getString(SharedPrefManager.AUTH_TOKEN_KEY, "")!!.isNotEmpty()) {
                    val userName = user.displayName
                    if(!userName.isNullOrEmpty()) {
                        sharedPref.saveString(SharedPrefManager.NAME_KEY, userName ?: "")
                        startActivity(Intent(this, HomeActivity::class.java))
                    } else {
                        startActivity(Intent(this, ProfileSetupActivity::class.java))
                    }
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2500)

        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpened
        }
    }
}