package com.app.picviewr.presenter

import com.app.picviewr.module.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterPresenter (private var view: RegisterActivity, private var auth: FirebaseAuth) {

    fun createAccount( email: String, password: String, ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(view) { task ->
                view.updateView(task.isSuccessful)
            }
    }

}