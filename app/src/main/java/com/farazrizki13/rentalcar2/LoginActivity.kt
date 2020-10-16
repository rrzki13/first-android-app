package com.farazrizki13.rentalcar2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.farazrizki13.rentalcar2.Api.RetrofitClient
import com.farazrizki13.rentalcar2.Model.LoginModel
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        et_username_login.setOnFocusChangeListener { view, b ->
            if (b) {
                et_username_login.setTextColor(resources.getColor(R.color.black))
                et_username_login.setHintTextColor(resources.getColor(R.color.black))
            }else {
                et_username_login.setTextColor(resources.getColor(R.color.white))
                et_username_login.setHintTextColor(resources.getColor(R.color.white))
            }
        }

        et_password_login.setOnFocusChangeListener { view, b ->
            if (b) {
                et_password_login.setTextColor(resources.getColor(R.color.black))
                et_password_login.setHintTextColor(resources.getColor(R.color.black))
            }else {
                et_password_login.setTextColor(resources.getColor(R.color.white))
                et_password_login.setHintTextColor(resources.getColor(R.color.white))
            }
        }

        to_regist.setOnClickListener {
            startActivity(Intent(this, RegistActivity::class.java))
        }

        btn_login.setOnClickListener {
            when {
                et_username_login.text.isEmpty() -> {
                    et_username_login.error = "Is Required"
                    et_username_login.requestFocus()
                }
                et_password_login.text.isEmpty() -> {
                    et_password_login.error = "Is Required"
                    et_password_login.requestFocus()
                }
                else -> {
                    showDialog()
                    RetrofitClient.instances.login(
                        et_username_login.text.toString(),
                        et_password_login.text.toString()
                    ).enqueue(object : Callback<LoginModel> {
                        override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                            hideDialog()
                            Toast.makeText(this@LoginActivity,t.localizedMessage, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<LoginModel>,
                            response: Response<LoginModel>
                        ) {
                            hideDialog()
                            if (response.body()?.response == true) {
                                Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_SHORT).show()

                                val id = response.body()?.session?.get(0)?.id
                                val username = response.body()?.session?.get(0)?.username
                                val nama = response.body()?.session?.get(0)?.nama
                                val email = response.body()?.session?.get(0)?.email
                                val level = response.body()?.session?.get(0)?.level
                                // put session to local storage
                                val sharePreferences : SharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE)
                                val editor : SharedPreferences.Editor = sharePreferences.edit()
                                editor.putBoolean("IS_LOGIN", true)
                                editor.putString("ID", id)
                                editor.putString("USERNAME", username)
                                editor.putString("NAMA", nama)
                                editor.putString("EMAIL", email)
                                editor.putString("LEVEL", level)
                                editor.apply()

                                // move activity
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            }else {
                                Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                            }
                        }

                    })
                }
            }
        }

    }

    override fun onBackPressed() {
        if (et_username_login.isFocusable || et_password_login.isFocusable) {
            super.onBackPressed()
        }else {
            finish()
        }
    }

    private fun showDialog () {
        the_dialog?.visibility = View.VISIBLE
        set_dark_bg?.visibility = View.VISIBLE
        et_username_login.isClickable = false
        et_username_login.isEnabled = false
        et_password_login.isClickable = false
        et_password_login.isEnabled = false
        btn_login.isClickable = false
        btn_login.isEnabled = false
        to_regist.isClickable = false
        to_regist.isEnabled = false
    }

    private fun hideDialog () {
        the_dialog?.visibility = View.GONE
        set_dark_bg?.visibility = View.GONE
        et_username_login.isClickable = true
        et_username_login.isEnabled = true
        et_password_login.isClickable = true
        et_password_login.isEnabled = true
        btn_login.isClickable = true
        btn_login.isEnabled = true
        to_regist.isClickable = false
        to_regist.isEnabled = false
    }
}