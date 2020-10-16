package com.farazrizki13.rentalcar2.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farazrizki13.rentalcar2.Model.HistoryModel
import com.farazrizki13.rentalcar2.Model.MyOrderModel
import com.farazrizki13.rentalcar2.R
import kotlinx.android.synthetic.main.rv_history.view.*

class HistoryAdapter(val items : ArrayList<HistoryModel>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){
    inner class HistoryViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        fun bind (historyModel: HistoryModel) {
            with(itemView) {
                val id = historyModel.id
                val kode_transaksi = historyModel.kode_transaksi
                val nama_mobil = historyModel.nama_mobil
                val nama_pemesan = historyModel.nama_pemesan
                val tgl_penyewaan = historyModel.tgl_penyewaan
                val tgl_mobil_kembali = historyModel.tgl_mobil_kembali
                val denda = historyModel.denda
                val lama_denda = historyModel.lama_denda
                val total = historyModel.total

                if (denda == "0") {
                    history_denda.visibility = View.GONE
                    history_lama_denda.visibility = View.GONE
                }else {
                    history_denda.visibility = View.VISIBLE
                    history_denda.setTextColor(resources.getColor(R.color.danger))
                    history_lama_denda.visibility = View.VISIBLE
                }

                history_id.text = id
                history_kode_transaksi.text = kode_transaksi
                history_nama_pemesan.text = nama_pemesan
                history_total.text = total
                history_nama_mobil.text = nama_mobil
                history_tgl_sewa.text = "tanggal pesan : $tgl_penyewaan"
                history_tgl_kembali.text = "tanggal kembali : $tgl_mobil_kembali"
                history_denda.text = "denda : $denda"
                history_lama_denda.text = "lama denda : $lama_denda"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }
}