package com.farazrizki13.rentalcar2.Api

import com.farazrizki13.rentalcar2.Model.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @FormUrlEncoded
    @POST("regist.php?key=13102004")
    fun regist(
        @Field("username") username : String,
        @Field("password") password : String,
        @Field("name") name : String,
        @Field("email") email: String
    ) : Call<RegistModel>

    @FormUrlEncoded
    @POST("login.php?key=13102004")
    fun login (
        @Field("username") username : String,
        @Field("password") password : String
    ) : Call<LoginModel>

    @GET("mobil.php?key=13102004")
    fun getMobil() : Call<ArrayList<MobilModel>>

    @FormUrlEncoded
    @POST("getMobil.php?key=13102004")
    fun getMobilById(
        @Field("id") id : Int
    ) : Call<ArrayList<GetMobilModel>>

    @FormUrlEncoded
    @POST("SewaMobil.php?key=13102004")
    fun sewaMobil(
        @Field("id_user") id_user : Int,
        @Field("id_mobil") id_mobil : Int,
        @Field("tgl_awal") tgl_awal : String,
        @Field("tgl_akhir") tgl_akhir : String,
        @Field("harga") harga : String,
        @Field("nama_penyewa") nama_penyewa : String
    ) : Call<SewaMobilModel>

    @FormUrlEncoded
    @POST("myOrder.php?key=13102004")
    fun myOrder (
        @Field("id_user") id_user: Int
    ) : Call<ArrayList<MyOrderModel>>

    @FormUrlEncoded
    @POST("MyHistory.php?key=13102004")
    fun history(
        @Field("id_user") id_user: Int
    ) : Call<ArrayList<HistoryModel>>
}