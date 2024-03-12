package com.app.picviewr.presenter

import com.app.picviewr.module.ProfileSetupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class ProfileSetupPresenter (private var view: ProfileSetupActivity, private var auth: FirebaseAuth) {

    fun updateName(name: String) {
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                view.updateView(task.isSuccessful) }
    }

}