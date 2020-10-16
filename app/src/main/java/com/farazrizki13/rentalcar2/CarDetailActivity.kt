package com.farazrizki13.rentalcar2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.farazrizki13.rentalcar2.Api.RetrofitClient
import com.farazrizki13.rentalcar2.Model.GetMobilModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_car_detail.*
import kotlinx.android.synthetic.main.activity_car_detail.set_dark_bg
import kotlinx.android.synthetic.main.activity_car_detail.the_dialog
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)



        val id_mobil : String? = intent.getStringExtra("id")
        if (id_mobil != null) {
            setData(id_mobil.toString())

            tv_deskripsi_car.setOnClickListener {
                menu1()
            }

            tv_detail_car.setOnClickListener {
                menu2()
            }

            btn_sewa.setOnClickListener {
                val intent : Intent = Intent(this, SewaActivity::class.java)
                intent.putExtra("id_mobil", id_mobil)
                intent.putExtra("harga_per_day", harga_per_day.text.toString())
                intent.putExtra("gambar", gambar_send.text.toString())
                startActivity(intent)
            }
        }

    }

    private fun menu1() {
        tv_deskripsi_car.setTextColor(resources.getColor(R.color.colorPrimary))
        hr_deksripsi.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
        menu1.visibility = View.VISIBLE
        menu1.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
        tv_detail_car.setTextColor(resources.getColor(R.color.gray))
        hr_detail.animate().alpha(0F).setDuration(350).setStartDelay(200).start()
        menu2.visibility = View.GONE
        menu2.animate().alpha(0F).setDuration(350).setStartDelay(200).start()
    }

    private fun menu2() {
        tv_detail_car.setTextColor(resources.getColor(R.color.colorPrimary))
        hr_detail.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
        menu2.visibility = View.VISIBLE
        menu2.animate().alpha(1F).setDuration(350).setStartDelay(200).start()
        tv_deskripsi_car.setTextColor(resources.getColor(R.color.gray))
        hr_deksripsi.animate().alpha(0F).setDuration(350).setStartDelay(200).start()
        menu1.visibility = View.GONE
        menu1.animate().alpha(0F).setDuration(350).setStartDelay(200).start()
    }

    private fun setData(id : String) {
        showDialog()
        RetrofitClient.instances.getMobilById(id.toInt()).enqueue(object : Callback<ArrayList<GetMobilModel>>{
            override fun onFailure(call: Call<ArrayList<GetMobilModel>>, t: Throwable) {
                hideDialog()
                Toast.makeText(this@CarDetailActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ArrayList<GetMobilModel>>,
                response: Response<ArrayList<GetMobilModel>>
            ) {
                val gambar = "http://192.168.43.203//rental_mobil_ci4/public/mobil/${response.body()?.get(0)?.gambar}"
                Picasso.get()
                    .load(gambar)
                    .fit()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(detail_car_img)

                var f_supir = ""
                var f_bensin = ""
                if(response.body()?.get(0)?.f_supir?.toInt() == 1) {
                    f_supir = "Yes"
                }else {
                    f_supir = "No"
                }

                if(response.body()?.get(0)?.f_bensin?.toInt() == 1) {
                    f_bensin = "Yes"
                }else {
                    f_bensin = "No"
                }

                val detail =
                    "Merek mobil : ${response.body()?.get(0)?.merek}\nKapasitas : ${response.body()?.get(0)?.kapasitas} orang\nTahun : ${response.body()?.get(0)?.tahun}\nFasilitas\nBensin : $f_bensin\nSupir : $f_supir"

                detail_car_nama_mobil.text = response.body()?.get(0)?.nama_mobil
                detail_car_harga.text = "IDR. ${response.body()?.get(0)?.harga} / day"
                detail_car_deskripsi.text = response.body()?.get(0)?.deskripsi
                detail_car_detail.text = detail
                harga_per_day.text = response.body()?.get(0)?.harga
                gambar_send.text = response.body()?.get(0)?.gambar

                hideDialog()
            }

        })
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