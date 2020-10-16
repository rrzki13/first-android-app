package com.farazrizki13.rentalcar2.Model

data class OrderDataModel(
    val id : String,
    val nama_mobil : String,
    val id_user : String,
    val nama_penyewa : String,
    val tgl_order : String,
    val tgl_kembali : String,
    val tgl_pembayaran : String,
    val status_pembayaran : String,
    val status_transaksi : String
)