package com.mumaza.movapps.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mumaza.movapps.R
import com.mumaza.movapps.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_two.*

class OnboardingTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_two)

    btn_lanjut.setOnClickListener{
        startActivity(
            Intent(this@OnboardingTwoActivity, OnboardingThreeActivity::class.java)
    )}
    btn_lewati.setOnClickListener{
        startActivity(Intent(this@OnboardingTwoActivity, SignInActivity::class.java))
    }
    }
}