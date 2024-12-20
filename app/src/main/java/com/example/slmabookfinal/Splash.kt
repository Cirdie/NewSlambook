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

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lottieAnimationView = binding.lottieAnimation

        lottieAnimationView.setAnimation(R.raw.loading_animation) // Load animation
        lottieAnimationView.playAnimation()

        lottieAnimationView.speed = 1.5f
        lottieAnimationView.repeatCount = LottieDrawable.INFINITE // Loop infinitely

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@Splash, ChooseActivity::class.java))
            finish()
        }, 5000)
    }
}
