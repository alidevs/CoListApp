package com.alidevs.colistapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alidevs.colistapp.databinding.ActivityRegisterBinding
import com.alidevs.colistapp.models.UserModel
import com.alidevs.colistapp.viewmodel.AuthenticationViewModel

class RegisterActivity : AppCompatActivity() {

	private lateinit var binding: ActivityRegisterBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityRegisterBinding.inflate(layoutInflater)

		setContentView(binding.root)
	}

	fun registerButtonPressed(view: View) {
		// Components
		val fullNameEditText = binding.registerFullnameTextField.editText!!
		val emailAddressEditText = binding.registerEmailTextField.editText!!
		val passwordEditText = binding.registerPasswordTextField.editText!!

		// TODO: Add linear progress bar

		// Variables
		val fullname = fullNameEditText.text.toString()
		val emailAddress = emailAddressEditText.text.toString()
		val password = passwordEditText.text.toString()
		val user = UserModel(fullname, emailAddress, password)

		// API
		val authenticatedUser = AuthenticationViewModel.userRegister(user)
		authenticatedUser.observe(this, {
			Toast.makeText(this, "Welcome to CoList, ${it.fullname}", Toast.LENGTH_SHORT).show()
			segueTo(HomeActivity::class.java)
		})
	}

	fun loginButtonPressed(view: View) {
		segueTo(LoginActivity::class.java)
	}

	private fun segueTo(activity: Class<*>) {
		val intent = Intent(this, activity)
		startActivity(intent)
	}
}