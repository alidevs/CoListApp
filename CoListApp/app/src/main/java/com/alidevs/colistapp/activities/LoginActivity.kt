package com.alidevs.colistapp.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.alidevs.colistapp.databinding.ActivityLoginBinding
import com.alidevs.colistapp.models.User
import com.alidevs.colistapp.utils.ApiViewModel

class LoginActivity : AppCompatActivity() {

	private lateinit var binding: ActivityLoginBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityLoginBinding.inflate(layoutInflater)

		setContentView(binding.root)
	}

	fun loginButtonPressed(view: View) {
		// Components
		val emailAddressEditText = binding.registerEmailTextField.editText!!
		val passwordEditText = binding.registerPasswordTextField.editText!!

		binding.linearProgressBar.visibility = View.VISIBLE

		// Variables
		val emailAddress = emailAddressEditText.text.toString()
		val password = passwordEditText.text.toString()
		val user = User(emailAddress, password)

		// API
		val authenticatedUser = ApiViewModel.userLogin(user)
		authenticatedUser.observe(this, {
			Log.d("Retrofit LoginActivity", "Got $it")
			Toast.makeText(this, "Welcome back, ${it.fullname}", Toast.LENGTH_SHORT).show()
			segueTo(MainActivity::class.java)
		})
	}

	fun registerButtonPressed(view: View) {
		segueTo(RegisterActivity::class.java)
	}

	private fun segueTo(activity: Class<*>) {
		val intent = Intent(this, activity)
		startActivity(intent)
	}

}