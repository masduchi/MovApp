package com.mumaza.movapps.sign.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import com.mumaza.movapps.R
import com.mumaza.movapps.sign.signin.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    lateinit var sUsername:String
    lateinit var sPassword:String
    lateinit var sNama:String
    lateinit var sEmail:String

    lateinit var mDatabaseReference : DatabaseReference
    lateinit var mFirebaseInstance : FirebaseDatabase
    lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mDatabaseReference = mFirebaseInstance.getReference("User")

        btn_lanjutkan.setOnClickListener {
            sUsername = et_username.text.toString()
            sPassword = et_password.text.toString()
            sNama = et_name.text.toString()
            sEmail = et_email.text.toString()

            if (sUsername.equals("")){
                et_username.error = "Silahkan isi username anda!"
                et_username.requestFocus()
            }else if (sPassword.equals("")){
                et_password.error = "Silahkan isi password anda!"
                et_password.requestFocus()
            }else if (sNama.equals("")){
                et_name.error = "Silahkan isi nama anda!"
                et_name.requestFocus()
            }else if (sEmail.equals("")){
                et_email.error = "Silahkan isi email anda!"
                et_email.requestFocus()
            }else{
                saveUsername (sUsername, sPassword, sNama, sEmail)
            }
        }
    }

    private fun saveUsername(sUsername: String, sPassword: String, sNama: String, sEmail: String) {
        var user = User()
        user.email = sEmail
        user.password = sPassword
        user.nama = sNama
        user.username = sUsername

        if (sUsername != null){
            checingUsername(sUsername, user)
        }
    }

    private fun checingUsername(sUsername: String, data: User) {
        mDatabaseReference.child(sUsername).addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
               var user = dataSnapshot.getValue(User::class.java)

                if (user == null){
                    mDatabaseReference.child(sUsername).setValue(data)

                    var goSignUpPhoto = Intent(this@SignUpActivity,
                        SignUpPhotoActivity::class.java).putExtra("nama", data.nama)
                        startActivity(goSignUpPhoto)
                } else {
                    Toast.makeText(this@SignUpActivity, "User sudah digunakan!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignUpActivity, ""+databaseError.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}