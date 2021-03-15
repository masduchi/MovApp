package com.mumaza.movapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.mumaza.movapps.checkout.PilihBangkuActivity
import com.mumaza.movapps.home.dashboard.PlaysAdapter
import com.mumaza.movapps.model.Film
import com.mumaza.movapps.model.Plays
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var mDatabase : DatabaseReference
    private var dataList = ArrayList<Plays>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val data = intent.getParcelableExtra<Film>("data")

        mDatabase = FirebaseDatabase.getInstance().getReference("Film")
            .child(data.judul.toString())
            .child("play")
        
        tv_kursi.text = data.judul.toString()
        tv_genre.text = data.genre
        tv_desc.text = data.desc
        tv_rate.text = data.rating

        Glide.with(this)
            .load(data.poster)
            .into(iv_poster)

        rv_who_play.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getData()

        btn_pilih_bangku.setOnClickListener{
            var intent = Intent(this@DetailActivity, PilihBangkuActivity::class.java).putExtra("data", data)
            startActivity(intent)
        }
    }

    private fun getData() {
       mDatabase.addValueEventListener(object : ValueEventListener{
           override fun onDataChange(dataSnapshot: DataSnapshot) {
               dataList.clear()
               for (getdataSnapshot in dataSnapshot.children){
                   var Film = getdataSnapshot.getValue(Plays::class.java)
                   dataList.add(Film!!)
               }

               rv_who_play.adapter = PlaysAdapter(dataList){

               }


           }

           override fun onCancelled(p0: DatabaseError) {
               TODO("Not yet implemented")
           }

       })
    }
}