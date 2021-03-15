package com.mumaza.movapps.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mumaza.movapps.R
import com.mumaza.movapps.sign.signin.SignInActivity
import com.mumaza.movapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnboardingOneActivity : AppCompatActivity() {
    lateinit var preferences:Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        preferences = Preferences(this)

        if (preferences.getValue("onboarding").equals("1")){
            finishAffinity()

            var intent = Intent( this@OnboardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }
        btn_lanjut.setOnClickListener{
            var intent = Intent( this@OnboardingOneActivity, OnboardingTwoActivity::class.java)
            startActivity(intent)
        }


        btn_lewat.setOnClickListener{
            preferences.setValues("onboarding", "1")
            finishAffinity()

            var intent = Intent( this@OnboardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}