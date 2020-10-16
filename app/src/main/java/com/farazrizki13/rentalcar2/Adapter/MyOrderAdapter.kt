package com.farazrizki13.rentalcar2.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farazrizki13.rentalcar2.Model.MobilModel
import com.farazrizki13.rentalcar2.Model.MyOrderModel
import com.farazrizki13.rentalcar2.Model.OrderDataModel
import com.farazrizki13.rentalcar2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_home.view.*
import kotlinx.android.synthetic.main.rv_mobil.view.*

class MyOrderAdapter(val items : ArrayList<MyOrderModel>) : RecyclerView.Adapter<MyOrderAdapter.MyOrderAdapterViewHolder>() {
    inner class MyOrderAdapterViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        fun bind (myOrderModel: MyOrderModel) {
            with(itemView) {
                val id = myOrderModel.id
                val nama_mobil = myOrderModel.nama_mobil
                val nama_penyewa = myOrderModel.nama_penyewa
                val status_pembayaran = myOrderModel.status_pembayaran.toInt()

                main_nama_mobil.setTextColor(resources.getColor(R.color.white))
                main_nama_pemesan.setTextColor(resources.getColor(R.color.white))
                main_status.setTextColor(resources.getColor(R.color.white))

                var the_status = ""
                if (status_pembayaran == 0) {
                    rv_home_card.setCardBackgroundColor(resources.getColor(R.color.danger))
                    the_status = "Belum Lunas"
                }else {
                    rv_home_card.setCardBackgroundColor(resources.getColor(R.color.success))
                    the_status = "Lunas"
                }

                main_id.text = id
                main_nama_mobil.text = nama_mobil
                main_nama_pemesan.text = nama_penyewa
                main_status.text = the_status

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_home, parent, false)
        return MyOrderAdapterViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyOrderAdapterViewHolder, position: Int) {
        holder.bind(items[position])
    }
}