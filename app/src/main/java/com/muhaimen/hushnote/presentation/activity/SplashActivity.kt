package com.muhaimen.hushnote.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.muhaimen.hushnote.R
import com.muhaimen.hushnote.viewModel.AuthViewModel

class SplashActivity : AppCompatActivity() {

    private var isSplashVisible = true
    private val  authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { isSplashVisible }

        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            isSplashVisible = false
            if (authViewModel.isUserLoggedIn()) {
                val intent = Intent(this, HomeActivity::class.java)
                val options = android.app.ActivityOptions.makeCustomAnimation(
                    this,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                startActivity(intent,options.toBundle())
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                val options = android.app.ActivityOptions.makeCustomAnimation(
                    this,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                startActivity(intent,options.toBundle())
            }
            finish()
        }, 1500)


    }
}