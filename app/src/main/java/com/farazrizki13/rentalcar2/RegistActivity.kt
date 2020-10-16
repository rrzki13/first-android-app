package com.farazrizki13.rentalcar2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.farazrizki13.rentalcar2.Api.RetrofitClient
import com.farazrizki13.rentalcar2.Model.RegistModel
import kotlinx.android.synthetic.main.activity_regist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistActivity : AppCompatActivity() {
    private val email_pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private val username_pattern = "[a-zA-Z0-9._-]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)

        et_username_regist.setOnFocusChangeListener { view, b ->
            if (b) {
                et_username_regist.setTextColor(resources.getColor(R.color.black))
                et_username_regist.setHintTextColor(resources.getColor(R.color.black))
            }else {
                et_username_regist.setTextColor(resources.getColor(R.color.white))
                et_username_regist.setHintTextColor(resources.getColor(R.color.white))
            }
        }

        et_email_regist.setOnFocusChangeListener { view, b ->
            if (b) {
                et_email_regist.setTextColor(resources.getColor(R.color.black))
                et_email_regist.setHintTextColor(resources.getColor(R.color.black))
            }else {
                et_email_regist.setTextColor(resources.getColor(R.color.white))
                et_email_regist.setHintTextColor(resources.getColor(R.color.white))
            }
        }

        et_name_regist.setOnFocusChangeListener { view, b ->
            if (b) {
                et_name_regist.setTextColor(resources.getColor(R.color.black))
                et_name_regist.setHintTextColor(resources.getColor(R.color.black))
            }else {
                et_name_regist.setTextColor(resources.getColor(R.color.white))
                et_name_regist.setHintTextColor(resources.getColor(R.color.white))
            }
        }

        et_password_regist.setOnFocusChangeListener { view, b ->
            if (b) {
                et_password_regist.setTextColor(resources.getColor(R.color.black))
                et_password_regist.setHintTextColor(resources.getColor(R.color.black))
            }else {
                et_password_regist.setTextColor(resources.getColor(R.color.white))
                et_password_regist.setHintTextColor(resources.getColor(R.color.white))
            }
        }

        to_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btn_regist.setOnClickListener {
            when {
                et_username_regist.text.isEmpty() -> {
                    et_username_regist.error = "Is Required"
                    et_username_regist.requestFocus()
                }
                validateUsername(et_username_regist.text.toString()) -> {
                    et_username_regist.error = "Without Spacing and weird character"
                    et_username_regist.requestFocus()
                }
                et_name_regist.text.isEmpty() -> {
                    et_name_regist.error = "Is Required"
                    et_name_regist.requestFocus()
                }
                et_email_regist.text.isEmpty() -> {
                    et_email_regist.error = "Is Required"
                    et_email_regist.requestFocus()
                }
                !validateEmail(et_email_regist.text.toString()) -> {
                    et_email_regist.error = "Invalid Email"
                    et_email_regist.requestFocus()
                }
                et_password_regist.text.isEmpty() -> {
                    et_password_regist.error = "Is Required"
                    et_password_regist.requestFocus()
                }
                else -> {
                    showDialog()
                    RetrofitClient.instances.regist(
                        et_username_regist.text.toString(),
                        et_password_regist.text.toString(),
                        et_name_regist.text.toString(),
                        et_email_regist.text.toString()
                    ).enqueue(object : Callback<RegistModel> {
                        override fun onFailure(call: Call<RegistModel>, t: Throwable) {
                            hideDialog()
                            Toast.makeText(this@RegistActivity, t.localizedMessage, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<RegistModel>,
                            response: Response<RegistModel>
                        ) {
                            hideDialog()
                            when {
                                response.body()!!.response -> {
                                    Toast.makeText(this@RegistActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@RegistActivity, LoginActivity::class.java))
                                    finish()
                                }
                                else -> {
                                    Toast.makeText(this@RegistActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })
                }
            }
        }

    }

    private fun validateEmail(email : String) : Boolean {
        return email.matches(email_pattern.toRegex())
    }

    private fun validateUsername(username : String) : Boolean {
        return username.matches(username_pattern.toRegex())
    }

    override fun onBackPressed() {
        if (et_username_regist.isFocusable || et_password_regist.isFocusable || et_name_regist.isFocusable || et_email_regist.isFocusable) {
            super.onBackPressed()
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun showDialog () {
        the_dialog_regist?.visibility = View.VISIBLE
        set_dark_bg_regist?.visibility = View.VISIBLE
        et_username_regist.isClickable = false
        et_username_regist.isEnabled = false
        et_name_regist.isClickable = false
        et_name_regist.isEnabled = false
        et_email_regist.isClickable = false
        et_email_regist.isEnabled = false
        et_password_regist.isClickable = false
        et_password_regist.isEnabled = false
        btn_regist.isClickable = false
        btn_regist.isEnabled = false
        to_login.isClickable = false
        to_login.isEnabled = false
    }

    private fun hideDialog () {
        the_dialog_regist?.visibility = View.GONE
        set_dark_bg_regist?.visibility = View.GONE
        et_username_regist.isClickable = true
        et_username_regist.isEnabled = true
        et_name_regist.isClickable = true
        et_name_regist.isEnabled = true
        et_email_regist.isClickable = true
        et_email_regist.isEnabled = true
        et_password_regist.isClickable = true
        et_password_regist.isEnabled = true
        btn_regist.isClickable = true
        btn_regist.isEnabled = true
        to_login.isClickable = true
        to_login.isEnabled = true
    }
}