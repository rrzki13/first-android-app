package com.farazrizki13.rentalcar2.Model

data class GetMobilModel(
    val id : String,
    val nama_mobil : String,
    val merek : String,
    val deskripsi : String,
    val kapasitas : String,
    val harga : String,
    val warna : String,
    val bensin : String,
    val tahun : String,
    val gambar : String,
    val f_supir : String,
    val f_bensin : String
)