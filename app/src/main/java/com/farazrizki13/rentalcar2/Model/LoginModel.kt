package com.farazrizki13.rentalcar2.Model

data class LoginModel (
    val message : String,
    val response : Boolean,
    val session : ArrayList<SessionModel>
)