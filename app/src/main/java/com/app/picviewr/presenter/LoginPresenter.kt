package com.app.picviewr.presenter

import com.app.picviewr.module.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class LoginPresenter (private var view: LoginActivity, private var auth: FirebaseAuth) {

    fun login( email: String, password: String, ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(view) { task ->
                view.updateView(task.isSuccessful)
            }
    }

}