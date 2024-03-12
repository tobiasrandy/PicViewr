package com.app.picviewr.module

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.app.picviewr.R
import com.app.picviewr.databinding.ActivityLoginBinding
import com.app.picviewr.presenter.LoginPresenter
import com.app.picviewr.util.SharedPrefManager
import com.app.picviewr.util.validateEmail
import com.app.picviewr.util.validatePassword
import com.app.picviewr.util.validateText
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter : LoginPresenter

    @Inject
    lateinit var authService: FirebaseAuth

    @Inject
    lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginPresenter(this, authService)
        authService.signOut()

        validateFields()

        binding.loginButton.setOnClickListener {
            login()
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == REGISTER_CODE) {
                showSnackbar(binding.root, "Account has been successfully created!", false)
            }
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(activity, RegisterActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun login() {
        val email = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val isValidEmail = validateEmail(email)
        val isValidPassword = validatePassword(password)

        if (isValidEmail && isValidPassword) {
            binding.loginButton.isEnabled = false
            binding.loginButton.text = ""
            binding.buttonProgressBar.visibility = View.VISIBLE
            hideKeyboard(binding.root)

            presenter.login(email, password)
        }
    }

    private fun validateFields() {
        binding.usernameEditText.validateText(
            { text -> validateEmail(text) },
            getString(R.string.invalid_email),
            binding.usernameInputLayout
        )
        binding.passwordEditText.validateText(
            { text -> validatePassword(text) },
            getString(R.string.invalid_password),
            binding.passwordInputLayout
        )
    }

    fun updateView(isSuccess: Boolean) {
        if(isSuccess) {
            val user = authService.currentUser
            if (user != null) {
                user.getIdToken(true)
                    .addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val token = tokenTask.result?.token
                            if(token!!.isNotEmpty()) {
                                val userEmail = user.email
                                val userName = user.displayName

                                sharedPref.saveString(SharedPrefManager.EMAIL_KEY, userEmail ?: "")
                                sharedPref.saveString(SharedPrefManager.AUTH_TOKEN_KEY, token)
                                val intent = if(userName.isNullOrEmpty()) {
                                    Intent(activity, ProfileSetupActivity::class.java)
                                } else {
                                    sharedPref.saveString(SharedPrefManager.NAME_KEY, userName ?: "")
                                    Intent(activity, HomeActivity::class.java)
                                }
                                startActivity(intent)
                                finish()
                            } else {
                                showLoginError()
                            }
                        } else {
                            showLoginError()
                        }
                    }
            } else showLoginError()
        } else {
            showLoginError()
        }
    }

    private fun showLoginError() {
        binding.buttonProgressBar.visibility = View.GONE
        binding.loginButton.isEnabled = true
        binding.loginButton.text = resources.getString(R.string.log_in)
        showSnackbar(binding.root, "Authentication failed", true)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val REGISTER_CODE = 21
    }
}