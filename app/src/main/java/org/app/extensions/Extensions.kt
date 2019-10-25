package org.app.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.content.ContextWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import org.app.demo.android.R
import org.app.common.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.getActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) {
            return ctx as Activity
        }
        ctx = (ctx as ContextWrapper).baseContext
    }
    return null
}


fun Context.getFragmentManager(): FragmentManager? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is FragmentActivity) {
            return ctx.supportFragmentManager
        }
        ctx = ctx.baseContext
    }
    return null
}


/**
 * Hide soft keyboard from activity
 */
fun Activity.hideSoftKeyBoard() {
    currentFocus?.apply {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(windowToken, 0)
        clearFocus()
    }
}

/**
 * Hide soft keyboard from view
 */
fun View.hideSoftKeyBoard() {
    context.getActivity()?.hideSoftKeyBoard()
}

fun Context.getHeightScreen(): Int {
    val metrics = this.resources.displayMetrics
    return metrics.heightPixels
}


fun Context.getWidthScreen(): Int {
    val metrics = this.resources.displayMetrics
    return metrics.widthPixels
}

/**
 * Get deviceId when login app
 */
@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String {
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}

fun Context.getStringResources(id: Int): String {
    return resources.getString(id)
}
