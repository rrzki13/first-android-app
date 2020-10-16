package com.farazrizki13.rentalcar2.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.farazrizki13.rentalcar2.CarDetailActivity
import com.farazrizki13.rentalcar2.Model.MobilModel
import com.farazrizki13.rentalcar2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_mobil.view.*

class MobilAdapter(val items : ArrayList<MobilModel>) : RecyclerView.Adapter<MobilAdapter.MobilAdapaterViewHolder>() {

    inner class MobilAdapaterViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        fun bind (mobilModel: MobilModel) {
            with(itemView) {
                val id = mobilModel.id
                val nama_mobil = mobilModel.nama_mobil
                val merek_mobil = mobilModel.merek
                val kapasitas = "${mobilModel.kapasitas} orang"
                val harga = mobilModel.harga
                val gambar = "http://192.168.43.203//rental_mobil_ci4/public/mobil/${mobilModel.gambar}"

                tv_card_id_mobil.text = id
                tv_card_nama_mobil.text = nama_mobil
                tv_card_merek_mobil.text = merek_mobil
                tv_card_kapasitas_mobil.text = kapasitas
                tv_card_harga_mobil.text = "IDR. $harga"

                Picasso.get()
                    .load(gambar)
                    .fit()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(img_card_mobil)

                itemView.setOnClickListener {
                    detail(id, context)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MobilAdapaterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_mobil, parent, false)
        return MobilAdapaterViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MobilAdapaterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    private fun detail(id : String, context : Context) {
        val intent : Intent= Intent(context, CarDetailActivity::class.java)
        intent.putExtra("id", id)
        context.startActivity(intent)
    }

}