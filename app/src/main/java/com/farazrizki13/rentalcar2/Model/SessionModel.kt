package com.farazrizki13.rentalcar2.Model

data class SessionModel(
    val id : String,
    val username : String,
    val password : String,
    val nama : String,
    val email : String,
    val level : String,
    val created_at : String,
    val updated_at : String
)