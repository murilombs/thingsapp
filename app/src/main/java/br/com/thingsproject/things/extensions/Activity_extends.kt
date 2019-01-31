package br.com.thingsproject.things.extensions

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import br.com.thingsproject.things.R
import kotlinx.android.synthetic.main.activity_main.view.*

// findViewById + setOnClickListner
fun AppCompatActivity.onClick(@IdRes viewId: Int, onClick: (v: android.view.View?) -> Unit) {
    val view = findViewById<View>(viewId)
    view.setOnClickListener { onClick(it) }
}

//adiciona o fragment ao layout
fun AppCompatActivity.addFragment(@IdRes layout: Int, fragment: Fragment) {
    fragment.arguments = intent.extras
    //val ft = supportFragmentManager.beginTransaction()
    val ft = supportFragmentManager.beginTransaction()
    ft.add(layout, fragment)
    ft.commit()
}

fun AppCompatActivity.setupToolbar(@IdRes id: Int, title: String? = null, upNavigation: Boolean = true): ActionBar? {
    val toolbar = findViewById<Toolbar>(id)
    setSupportActionBar(toolbar)
    if (title != null) {
        supportActionBar?.title = title
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(upNavigation)
    return supportActionBar
}

