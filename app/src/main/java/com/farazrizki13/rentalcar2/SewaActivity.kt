package com.farazrizki13.rentalcar2

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
import com.farazrizki13.rentalcar2.Api.RetrofitClient
import com.farazrizki13.rentalcar2.Model.SewaMobilModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sewa.*
import kotlinx.android.synthetic.main.activity_sewa.set_dark_bg
import kotlinx.android.synthetic.main.activity_sewa.the_dialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class SewaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sewa)

        val sharedPreferences : SharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE)
        val id_user = sharedPreferences.getString("ID", "").toString()
        val nama_user = sharedPreferences.getString("NAMA", "").toString()
        val id_mobil : String? = intent.getStringExtra("id_mobil")
        val harga : String? = intent.getStringExtra("harga_per_day")
        val gambar : String? = intent.getStringExtra("gambar")

        et_nama_penyewa.setText(nama_user)

        Picasso.get()
            .load("http://192.168.43.203//rental_mobil_ci4/public/mobil/$gambar")
            .fit()
            .placeholder(R.drawable.ic_launcher_background)
            .into(sewa_img)

        sewa_harga_per_hari.text = harga.toString()
        sewa_id_mobil.text = id_mobil.toString()

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        sewa_tgl_awal.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                val new_month = monthOfYear + 1
                var text = "$year-$new_month-$dayOfMonth"
                when {
                    new_month < 10 && dayOfMonth < 10 -> {
                        text = "$year-0$new_month-0$dayOfMonth"
                    }
                    new_month < 10 -> {
                        text = "$year-0$new_month-$dayOfMonth"
                    }
                    dayOfMonth < 10 -> {
                        text = "$year-$new_month-0$dayOfMonth"
                    }
                }
                tv_tgl_awal.text = text
                et_sewa_tgl_awal.setText(text)
            }, year, month, day)
            dpd.show()
        }

        sewa_tgl_akhir.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                val new_month = monthOfYear + 1
                var text = "$year-$new_month-$dayOfMonth"
                when {
                    new_month < 10 && dayOfMonth < 10 -> {
                        text = "$year-0$new_month-0$dayOfMonth"
                    }
                    new_month < 10 -> {
                        text = "$year-0$new_month-$dayOfMonth"
                    }
                    dayOfMonth < 10 -> {
                        text = "$year-$new_month-0$dayOfMonth"
                    }
                }
                tv_tgl_akhir.text = text
                et_sewa_tgl_akhir.setText(text)
            }, year, month, day)
            dpd.show()
        }

        btn_sewa_mobil.setOnClickListener {
            when {
                et_nama_penyewa.text.isEmpty() -> {
                    et_nama_penyewa.error = "Is Required"
                    et_nama_penyewa.requestFocus()
                }
                et_sewa_tgl_awal.text.isEmpty() -> {
                    Toast.makeText(this, "Tanggal awal is Required", Toast.LENGTH_SHORT).show()
                }
                et_sewa_tgl_akhir.text.isEmpty() -> {
                    Toast.makeText(this, "Tanggal akhir is Required", Toast.LENGTH_SHORT).show()
                }
                et_sewa_tgl_awal.text == et_sewa_tgl_akhir.text -> {
                    Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    showDialog()
                    RetrofitClient.instances.sewaMobil(
                        id_user.toInt(),
                        sewa_id_mobil.text.toString().toInt(),
                        et_sewa_tgl_awal.text.toString(),
                        et_sewa_tgl_akhir.text.toString(),
                        sewa_harga_per_hari.text.toString(),
                        nama_user
                    ).enqueue(object : Callback<SewaMobilModel>{
                        override fun onFailure(call: Call<SewaMobilModel>, t: Throwable) {
                            Toast.makeText(this@SewaActivity, t.localizedMessage, Toast.LENGTH_LONG).show()
                            hideDialog()
                        }

                        override fun onResponse(
                            call: Call<SewaMobilModel>,
                            response: Response<SewaMobilModel>
                        ) {
                            if (response.body()!!.status) {
                                Toast.makeText(this@SewaActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@SewaActivity , MainActivity::class.java))
                                finish()
                            }else {
                                Toast.makeText(this@SewaActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                                hideDialog()
                            }
                        }

                    })
                }
            }
        }

    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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