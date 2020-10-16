package com.farazrizki13.rentalcar2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.farazrizki13.rentalcar2.Adapter.MyOrderAdapter
import com.farazrizki13.rentalcar2.Api.RetrofitClient
import com.farazrizki13.rentalcar2.Model.MobilModel
import com.farazrizki13.rentalcar2.Model.MyOrderModel
import com.farazrizki13.rentalcar2.Model.OrderDataModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.set_dark_bg
import kotlinx.android.synthetic.main.activity_login.the_dialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash_screen.view.*
import kotlinx.android.synthetic.main.content_car.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private val list  = ArrayList<MyOrderModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val sharedPreferences : SharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE)
        val login = sharedPreferences.getBoolean("IS_LOGIN", false)

        if (login) {
            setData()
        }else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        nav_view.setNavigationItemSelectedListener(this)

        swl_main.setOnRefreshListener {
            swl_main.isRefreshing = true
            setData()
            swl_main.isRefreshing = false
        }

    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        val sharedPreferences : SharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE)
        val id_user = sharedPreferences.getString("ID", "")
        rv_home.setHasFixedSize(true)
        rv_home.layoutManager = LinearLayoutManager(this)
        img_no_internet.visibility = View.GONE
        img_no_internet.animate().alpha(0F).setDuration(350).setStartDelay(200).start()
        tv_no_internet.visibility = View.GONE
        tv_no_internet.animate().alpha(0F).setDuration(350).setStartDelay(200).start()

        img_no_order.visibility = View.GONE
        img_no_order.animate().alpha(0F).setDuration(350).setStartDelay(200).start()
        tv_no_order.visibility = View.GONE
        tv_no_order.animate().alpha(0F).setDuration(350).setStartDelay(200).start()

        showDialog()
        RetrofitClient.instances.myOrder(id_user = id_user.toString().toInt()).enqueue(object : Callback<ArrayList<MyOrderModel>>{
            override fun onFailure(call: Call<ArrayList<MyOrderModel>>, t: Throwable) {
                hideDialog()
                list.clear()
                img_no_internet.visibility = View.VISIBLE
                img_no_internet.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
                tv_no_internet.visibility = View.VISIBLE
                tv_no_internet.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
                Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ArrayList<MyOrderModel>>,
                response: Response<ArrayList<MyOrderModel>>
            ) {
                if (response.body()?.get(0)!!.response){
                    hideDialog()
                    list.clear()
                    response.body()?.let { list.addAll(it) }
                    val adapter = MyOrderAdapter(list)
                    rv_home.adapter = adapter
                }else {
                    list.clear()
                    img_no_order.visibility = View.VISIBLE
                    img_no_order.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
                    tv_no_order.visibility = View.VISIBLE
                    tv_no_order.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
                    hideDialog()
                }
            }

        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_home -> {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_car -> {
                startActivity(Intent(this, CarActivity::class.java))
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
            R.id.nav_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }

        return true
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