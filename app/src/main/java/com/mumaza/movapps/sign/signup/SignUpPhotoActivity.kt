package com.mumaza.movapps.sign.signup

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mumaza.movapps.home.HomeActivity
import com.mumaza.movapps.R
import com.mumaza.movapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_up_photo.*
import java.util.*

class SignUpPhotoActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd:Boolean = false
    lateinit var filePath:Uri
    lateinit var storage : FirebaseStorage
    lateinit var storageReferensi : StorageReference
    lateinit var preferences : Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photo)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReferensi = storage.getReference()

        tv_hello.text = "Selamat Datang\n"+intent.getStringExtra("nama")

        iv_add.setOnClickListener{
           if (statusAdd){
               statusAdd = false
               btn_save.visibility = View.INVISIBLE
               iv_add.setImageResource(R.drawable.btn_add)
               iv_profil.setImageResource(R.drawable.user)
           }else{
               Dexter.withActivity(this)
                   .withPermission(android.Manifest.permission.CAMERA)
                   .withListener(this)
                   .check()
           }
        }

        btn_skip.setOnClickListener{
            finishAffinity()

            var goHome = Intent (this@SignUpPhotoActivity, HomeActivity::class.java)
            startActivity(goHome)
        }

        btn_save.setOnClickListener{
            if (filePath != null){
                var progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading....")
                progressDialog.show()

                var ref = storageReferensi.child("images/"+ UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValues("url", it.toString())
                        }

                        finishAffinity()
                        var goHome = Intent( this@SignUpPhotoActivity, HomeActivity::class.java)
                        startActivity(goHome)
                    }
                    .addOnFailureListener{
                        progressDialog.dismiss()
                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener {
                        taskSnapshot -> var progress = 100.0 * taskSnapshot.bytesTransferred/taskSnapshot.totalByteCount
                        progressDialog.setMessage("Upload"+progress.toInt()+"%")
                    }
            } else {

            }
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
       Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{
           takePictureIntent ->
           takePictureIntent.resolveActivity(packageManager)?.also {
               startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
           }
       }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa menambahkan photo profil!", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Tergesah? Klik tombol upload nanti", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
        TODO("Not yet implemented")
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
           var bitmap = data?.extras?.get("data") as Bitmap
           statusAdd = true

           filePath = data.getData()!!
           Glide.with(this)
               .load(bitmap)
               .apply(RequestOptions.circleCropTransform())
               .into(iv_profil)

           btn_save.visibility = View.VISIBLE
           iv_add.setImageResource(R.drawable.ic_btn_delete)
       }
    }
}