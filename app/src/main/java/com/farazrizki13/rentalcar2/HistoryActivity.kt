package com.farazrizki13.rentalcar2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.farazrizki13.rentalcar2.Adapter.HistoryAdapter
import com.farazrizki13.rentalcar2.Adapter.MyOrderAdapter
import com.farazrizki13.rentalcar2.Api.RetrofitClient
import com.farazrizki13.rentalcar2.Model.HistoryModel
import com.farazrizki13.rentalcar2.Model.MyOrderModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.set_dark_bg
import kotlinx.android.synthetic.main.activity_login.the_dialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_history.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private val list  = ArrayList<HistoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "History"

        val sharedPreferences : SharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE)
        val login = sharedPreferences.getBoolean("IS_LOGIN", false)

        if (login) {
            setData()
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener(this)

        swl_history.setOnRefreshListener {
            swl_history.isRefreshing = true
            setData()
            swl_history.isRefreshing = false
        }
    }

    private fun setData() {
        showDialog()
        val sharedPreferences : SharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("ID", "")

        rv_history.setHasFixedSize(true)
        rv_history.layoutManager = LinearLayoutManager(this)

        img_no_internet_history.visibility = View.GONE
        tv_no_internet_history.visibility = View.GONE
        img_no_internet_history.animate().alpha(0F).setDuration(350).setStartDelay(200).start()
        tv_no_internet_history.animate().alpha(0F).setDuration(350).setStartDelay(200).start()
        img_no_history.visibility = View.GONE
        tv_no_history.visibility = View.GONE
        img_no_history.animate().alpha(0F).setDuration(350).setStartDelay(200).start()
        tv_no_history.animate().alpha(0F).setDuration(350).setStartDelay(200).start()

        RetrofitClient.instances.history(id.toString().toInt()).enqueue(object : Callback<ArrayList<HistoryModel>>{
            override fun onFailure(call: Call<ArrayList<HistoryModel>>, t: Throwable) {
                hideDialog()
                list.clear()
                img_no_internet_history.visibility = View.VISIBLE
                tv_no_internet_history.visibility = View.VISIBLE
                img_no_internet_history.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
                tv_no_internet_history.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
                Toast.makeText(this@HistoryActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ArrayList<HistoryModel>>,
                response: Response<ArrayList<HistoryModel>>
            ) {
                if (response.body()?.get(0)!!.status){
                    hideDialog()
                    list.clear()
                    response.body()?.let { list.addAll(it) }
                    val adapter = HistoryAdapter(list)
                    rv_history.adapter = adapter
                }else{
                    hideDialog()
                    list.clear()
                    img_no_history.visibility = View.VISIBLE
                    tv_no_history.visibility = View.VISIBLE
                    img_no_history.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
                    tv_no_history.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        setData()
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.nav_car -> {
                startActivity(Intent(this, CarActivity::class.java))
            }
            R.id.nav_history -> {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_logout -> {
                val sharedPreferences : SharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE)
                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.history ->{
                true
            }
            R.id.refres -> {
                setData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun showDialog () {
        the_dialog?.visibility = View.VISIBLE
        set_dark_bg?.visibility = View.VISIBLE
    }

    private fun hideDialog () {
        the_dialog?.visibility = View.GONE
        set_dark_bg?.visibility = View.GONE
    }
}