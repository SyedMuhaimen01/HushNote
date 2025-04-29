package com.muhaimen.hushnote.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.muhaimen.hushnote.R

class SplashActivity : AppCompatActivity() {

    private var isSplashVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { isSplashVisible }

        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            isSplashVisible = false
            val intent = Intent(this, HomeActivity::class.java)
            val options = android.app.ActivityOptions.makeCustomAnimation(
                this,
                R.anim.fade_in,
                R.anim.fade_out
            )
            startActivity(intent,options.toBundle())
            finish()
        }, 1500)
    }
}