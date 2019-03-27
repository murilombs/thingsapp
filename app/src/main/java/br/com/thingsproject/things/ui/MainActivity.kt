package br.com.thingsproject.things.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.widget.Toast
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.BaseActivity
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.domain.UserService.refreshToken
import br.com.thingsproject.things.extensions.disableShiftMode
import br.com.thingsproject.things.extensions.setupToolbar
import br.com.thingsproject.things.extensions.toJson
import br.com.thingsproject.things.fragment.*
import br.com.thingsproject.things.utils.PermissionUtils
import br.com.thingsproject.things.utils.Prefs
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Response
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class MainActivity : BaseActivity(),
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ItensFragement.OnNewSearch {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mGoogleApiClient : GoogleApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        refreshT()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Configura o googleApiClient
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        // permissoes
        PermissionUtils.validate(this, 0,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (result in grantResults){
            if (result == PackageManager.PERMISSION_DENIED) {
                alertAndFinish()
                return
            }
        }
    }

    private fun alertAndFinish() {
        run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name)
                    .setMessage("Para utilizar esse aplicativo você precissa aceitar as permissões")
            builder.setPositiveButton("OK") {dialog, id -> finish()}
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    override fun onStop() {
        stopLocationUpdates()
        mGoogleApiClient.disconnect()
        super.onStop()
    }

    override fun onConnected(p0: Bundle?) {
        // Inicia o GPS
        startLocationUpdates()
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d("GoogleAPI", "Conexão interrompida")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("GoogleAPI", "Erro ao conectar: ${p0}")
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
        //val bundle = Bundle().apply { putString("argument", item.toJson()) }
        Log.i("PutArgument", item.toJson()) // Ate aqui funciona
        fragment.arguments = bundle
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }

    // Listener para monitorar o GPS
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            showLocation(location)
        }
    }

    private fun showLocation(location: Location) {
        Log.d("Localização", "Lat/Lng: ${location.latitude}/${location.longitude}")
    }

    // Inicia o rastreamento
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locRequest = LocationRequest.create()
        locRequest.interval = 10000
        locRequest.fastestInterval = 5000
        locRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient.requestLocationUpdates(locRequest, locationCallback, Looper.myLooper())
    }

    private fun stopLocationUpdates() {
        val locClient = LocationServices.getFusedLocationProviderClient(this)
        locClient.removeLocationUpdates(locationCallback)
    }

    private fun toast(s: String) {
        Toast.makeText(baseContext, s , Toast.LENGTH_SHORT).show()
    }
}
