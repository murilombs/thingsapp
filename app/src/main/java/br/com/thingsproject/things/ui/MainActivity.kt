package br.com.thingsproject.things.ui


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.BaseActivity
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.domain.UserService.refreshToken
import br.com.thingsproject.things.extensions.disableShiftMode
import br.com.thingsproject.things.extensions.toJson
import br.com.thingsproject.things.fragment.*
import br.com.thingsproject.things.utils.Prefs
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class MainActivity : BaseActivity(), ItensFragement.OnNewSearch {
    override fun onCreate(savedInstanceState: Bundle?) {
        refreshT()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Salva o estado para o giro da tela
        if (savedInstanceState == null) {
            val fragment = ItensFragement()
            openFragment(fragment)
        }
        //val bottomNavigationView : BottomNavigationView = findViewById(R.id.navigationView)
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navigationView.disableShiftMode()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val fragment = ItensFragement()
                openFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_itens -> {
                val fragment = ItensParticulares()
                openFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chat -> {
                val fragment = FragmentListChat()
                openFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                val fragment = FragmentPerfil()
                openFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

    // Check Token
    private fun refreshT() {
        val token = Prefs.getString("token")
        doAsync {
            val response = refreshToken(token)
            if (response.status == "OK") {
                Prefs.setString("token", response.token)
            } else {
                uiThread {
                    startActivity<Login>()
                }
            }
        }
    }

    override fun OnsearchDone(item: List<Item>) {
        val fragment = ItensListConsult()
        val bundle = Bundle()
        bundle.putString("argument", item.toJson())
        fragment.arguments = bundle
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun toast(s: String) {
        Toast.makeText(baseContext, s , Toast.LENGTH_SHORT).show()
    }
}
