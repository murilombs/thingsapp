package br.com.thingsproject.things.extensions

import android.widget.TextView

var TextView.string: String
    get() = text.toString()
    set(value) { text = value}
fun TextView.isEmpty() = text.trim().isEmpty()