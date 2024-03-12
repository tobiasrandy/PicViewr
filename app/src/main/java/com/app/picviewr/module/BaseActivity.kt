package com.app.picviewr.module

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.picviewr.R
import com.app.picviewr.dialog.LoadingDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


open class BaseActivity: AppCompatActivity() {
    lateinit var activity: BaseActivity
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        loadingDialog = LoadingDialog(activity)

    }

    fun showSnackbar(view: View?, message: String?, isError: Boolean) {
        snackbar = Snackbar.make(view!!, message!!, Snackbar.LENGTH_SHORT)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        snackbar.getView().background = ContextCompat.getDrawable(this,
            R.drawable.snackbar_background
        )
        snackbar.setBackgroundTint(ContextCompat.getColor(this, if (isError) R.color.md_red_300 else R.color.md_green_A700))
        snackbar.show()
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}