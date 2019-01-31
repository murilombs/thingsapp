package br.com.thingsproject.things.extensions

import android.support.annotation.StringRes
import android.widget.Toast
import android.support.v4.app.Fragment

fun Fragment.toast(message: CharSequence, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(activity, message, length).show()

fun Fragment.toast(@StringRes message: Int, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(activity, message, length).show()