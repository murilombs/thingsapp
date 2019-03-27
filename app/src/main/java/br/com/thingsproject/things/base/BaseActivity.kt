package br.com.thingsproject.things.base

import android.content.Context
import android.support.v7.app.AppCompatActivity
import br.com.thingsproject.things.utils.Prefs

open class BaseActivity: AppCompatActivity() {
    protected val context: Context get() = this
    protected val token = Prefs.getString("token")
}