package br.com.thingsproject.things.base

import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import br.com.thingsproject.things.utils.Prefs

open class FragmentBasico: Fragment() {
    //metodos comuns
    protected val token = Prefs.getString("token")
    protected val TAG = "Things-API:: "
}