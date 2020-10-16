package com.farazrizki13.rentalcar2.Model

data class HistoryModel(
    val id : String,
    val status : Boolean,
    val kode_transaksi : String,
    val nama_mobil : String,
    val nama_pemesan : String,
    val tgl_penyewaan : String,
    val tgl_pengembalian : String,
    val tgl_mobil_kembali : String,
    val denda : String,
    val lama_denda : String,
    val total : String
)