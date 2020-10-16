package com.farazrizki13.rentalcar2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.farazrizki13.rentalcar2.Adapter.MobilAdapter
import com.farazrizki13.rentalcar2.Api.RetrofitClient
import com.farazrizki13.rentalcar2.Model.MobilModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_car.*
import kotlinx.android.synthetic.main.content_car.set_dark_bg
import kotlinx.android.synthetic.main.content_car.the_dialog
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private val list  = ArrayList<MobilModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "Sewa Mobil"

        drawerLayout = findViewById(R.id.drawer_layout)

        nav_view.setNavigationItemSelectedListener(this)

        swl_car.setOnRefreshListener {
            swl_car.isRefreshing = true
            setData()
            swl_car.isRefreshing = false
        }

        setData()
    }

    override fun onResume() {
        setData()
        super.onResume()
    }

    private fun setData() {
        set_dark_bg.visibility = View.VISIBLE
        the_dialog.visibility = View.VISIBLE

        tv_title_pesan.visibility = View.GONE
        img_no_internet_car.visibility = View.GONE
        img_no_internet_car.animate().alpha(0F).setDuration(350).setStartDelay(200).start()
        tv_no_internet_car.visibility = View.GONE
        tv_no_internet_car.animate().alpha(0F).setDuration(350).setStartDelay(200).start()

        rv_car.setHasFixedSize(true)
        rv_car.layoutManager = LinearLayoutManager(this)

        RetrofitClient.instances.getMobil().enqueue(object : Callback<ArrayList<MobilModel>>{
            override fun onFailure(call: Call<ArrayList<MobilModel>>, t: Throwable) {
                set_dark_bg.visibility = View.GONE
                the_dialog.visibility = View.GONE
                list.clear()

                img_no_internet_car.visibility = View.VISIBLE
                img_no_internet_car.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
                tv_no_internet_car.visibility = View.VISIBLE
                tv_no_internet_car.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
                Toast.makeText(this@CarActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ArrayList<MobilModel>>,
                response: Response<ArrayList<MobilModel>>
            ) {
                set_dark_bg.visibility = View.GONE
                the_dialog.visibility = View.GONE
                tv_title_pesan.visibility = View.VISIBLE
                list.clear()
                response.body()?.let { list.addAll(it) }
                val adapter = MobilAdapter(list)
                rv_car.adapter = adapter
            }

        })

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }
            R.id.nav_car -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            R.id.nav_logout -> {
                val sharedPreferences : SharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE)
                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            R.id.nav_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.history ->{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.refres -> {
                setData()
            }
            else -> super.onOptionsItemSelected(item)
        }

        return true
    }
}