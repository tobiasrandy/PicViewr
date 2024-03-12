package com.app.picviewr.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.picviewr.module.LoginActivity
import com.app.picviewr.databinding.FragmentProfileBinding
import com.app.picviewr.util.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var authService: FirebaseAuth

    @Inject
    lateinit var sharedPref: SharedPrefManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.nameText.text = sharedPref.getString(SharedPrefManager.NAME_KEY, "")
        binding.emailText.text = sharedPref.getString(SharedPrefManager.EMAIL_KEY, "")

        binding.logOut.setOnClickListener {
            showAlertDialog()
        }

        return binding.root
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        alertDialogBuilder.setTitle("Are you sure you want to log out?")

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.setPositiveButton("Log out") { dialog, _ ->
            dialog.dismiss()
            authService.signOut()
            sharedPref.clearData()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}