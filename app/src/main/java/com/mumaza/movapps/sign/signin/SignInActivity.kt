package com.mumaza.movapps.sign.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.mumaza.movapps.home.HomeActivity
import com.mumaza.movapps.R
import com.mumaza.movapps.sign.signup.SignUpActivity
import com.mumaza.movapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    lateinit var iUsername:String
    lateinit var iPassword:String

    lateinit var mDatabase: DatabaseReference
    lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        preferences = Preferences(this)

        preferences.setValues("onboarding", "1")
        if (preferences.getValue("status").equals("1")){
            finishAffinity()

            var goHome = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(goHome)
        }
        btn_masuk.setOnClickListener{
            iUsername = et_username.text.toString()
            iPassword = et_password.text.toString()

            if (iUsername.equals("")){
                et_username.error = "Username anda masih kosong!"
                et_username.requestFocus()
            }else if (iPassword.equals("")){
                et_password.error = "Password anda masih kosong!"
                et_password.requestFocus()
            }else{
              pushLogin(iUsername, iPassword)
            }
        }

        btn_daftar.setOnClickListener{
            var intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {
    mDatabase.child(iUsername).addValueEventListener(object :ValueEventListener{
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            var user = dataSnapshot.getValue(User::class.java)
            if (user == null){
                Toast.makeText(this@SignInActivity, "User tidak ditemukan", Toast.LENGTH_LONG).show()
            }else{

                if(user.password.equals(iPassword)) {

                    preferences .setValues("nama", user.nama.toString())
                    preferences .setValues("user", user.username.toString())
                    preferences .setValues("url", user.url.toString())
                    preferences .setValues("email", user.email.toString())
                    preferences .setValues("saldo", user.saldo.toString())
                    preferences .setValues("status", "1")

                    var intent = Intent(this@SignInActivity, HomeActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this@SignInActivity, "Password anda salah", Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Toast.makeText(this@SignInActivity, databaseError.message, Toast.LENGTH_LONG).show()
        }
    })
    }
}