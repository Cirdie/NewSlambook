package com.example.slmabookfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieDrawable
import com.example.slmabookfinal.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding // View Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Access LottieAnimationView
        val lottieAnimationView = binding.lottieAnimation

        // Start Lottie animation
        lottieAnimationView.setAnimation(R.raw.loading_animation) // Load animation
        lottieAnimationView.playAnimation() // Start animation

        // Customize Lottie animation
        lottieAnimationView.speed = 1.5f // Set playback speed
        lottieAnimationView.repeatCount = LottieDrawable.INFINITE // Loop infinitely

        // Navigate to the next activity after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@Splash, ChooseActivity::class.java))
            finish() // Remove SplashActivity from the back stack
        }, 5000) // Delay of 3 seconds
    }
}
