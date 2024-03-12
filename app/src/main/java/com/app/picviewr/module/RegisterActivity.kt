package com.app.picviewr.module

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import com.app.picviewr.R
import com.app.picviewr.databinding.ActivityRegisterBinding
import com.app.picviewr.presenter.RegisterPresenter
import com.app.picviewr.util.validateConfirmPassword
import com.app.picviewr.util.validateEmail
import com.app.picviewr.util.validatePassword
import com.app.picviewr.util.validateText
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter : RegisterPresenter

    @Inject
    lateinit var authService: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = RegisterPresenter(this, authService)

        validateFields()

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.signUpButton.setOnClickListener {
            createAccount()
        }

        binding.haveAccountButton.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun validateFields() {
        binding.emailEditText.validateText(
            { text -> validateEmail(text) },
            getString(R.string.invalid_email),
            binding.emailInputLayout
        )
        binding.passwordEditText.validateText(
            { text -> validatePassword(text) },
            getString(R.string.invalid_password),
            binding.passwordInputLayout
        )
        binding.confirmPasswordEditText.validateText(
            { text -> validateConfirmPassword(binding.passwordEditText.text.toString(), text) },
            getString(R.string.invalid_confirm_password),
            binding.confirmPasswordInputLayout
        )
    }

    private fun createAccount() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        val isValidEmail = validateEmail(email)
        val isValidPassword = validatePassword(password)
        val isValidConfirmPassword = validateConfirmPassword(password, confirmPassword)

        if (isValidEmail && isValidPassword && isValidConfirmPassword) {
            binding.signUpButton.isEnabled = false
            binding.signUpButton.text = ""
            binding.buttonProgressBar.visibility = View.VISIBLE
            hideKeyboard(binding.root)

            presenter.createAccount(email, password)
        }
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("Already have an account?")

        alertDialogBuilder.setNegativeButton("Continue creating account") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.setPositiveButton("Log in") { dialog, _ ->
            dialog.dismiss()
            finish()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun updateView(isSuccess: Boolean) {
        if(isSuccess) {
            setResult(LoginActivity.REGISTER_CODE)
            finish()
        } else {
            binding.buttonProgressBar.visibility = View.GONE
            binding.signUpButton.isEnabled = true
            binding.signUpButton.text = resources.getString(R.string.sign_up)
            showSnackbar(binding.root, "Authentication failed", true)
        }
    }
}