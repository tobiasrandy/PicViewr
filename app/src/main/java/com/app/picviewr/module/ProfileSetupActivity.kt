package com.app.picviewr.module

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.picviewr.R
import com.app.picviewr.databinding.ActivityProfileSetupBinding
import com.app.picviewr.presenter.ProfileSetupPresenter
import com.app.picviewr.util.SharedPrefManager
import com.app.picviewr.util.validateName
import com.app.picviewr.util.validateText
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileSetupActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileSetupBinding
    private lateinit var presenter : ProfileSetupPresenter

    @Inject
    lateinit var authService: FirebaseAuth

    @Inject
    lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = ProfileSetupPresenter(this, authService)

        validateFields()

        binding.submitButton.setOnClickListener {
            updateName()
        }

        binding.backButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            sharedPref.clearData()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateName() {
        val name = binding.nameEditText.text.toString()

        val isValidName = validateName(name)

        if (isValidName) {
            binding.submitButton.isEnabled = false
            binding.submitButton.text = ""
            binding.buttonProgressBar.visibility = View.VISIBLE
            hideKeyboard(binding.root)

            presenter.updateName(name)
        }
    }

    private fun validateFields() {
        binding.nameEditText.validateText(
            { text -> validateName(text) },
            getString(R.string.name_cannot_be_empty),
            binding.nameInputLayout
        )
    }

    fun updateView(isSuccess: Boolean) {
        if(isSuccess) {
            val user = authService.currentUser
            if (user != null) {
                val userName = user.displayName

                if(!userName.isNullOrEmpty()) {
                    sharedPref.saveString(SharedPrefManager.NAME_KEY, userName ?: "")
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showUpdateError()
                }
            } else showUpdateError()
        } else {
            showUpdateError()
        }
    }

    private fun showUpdateError() {
        binding.buttonProgressBar.visibility = View.GONE
        binding.submitButton.isEnabled = true
        binding.submitButton.text = resources.getString(R.string.submit)
        showSnackbar(binding.root, "Authentication failed", true)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }
}