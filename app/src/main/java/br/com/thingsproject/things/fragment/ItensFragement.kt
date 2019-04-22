package br.com.thingsproject.things.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.*
import android.widget.SearchView
import br.com.thingsproject.things.R
import br.com.thingsproject.things.adapter.CardAdapter
import br.com.thingsproject.things.base.FragmentBasico
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.domain.ItensService
import br.com.thingsproject.things.domain.Response
import br.com.thingsproject.things.extensions.toJson
import org.jetbrains.anko.startActivity
import br.com.thingsproject.things.ui.MaisInformacoes
import br.com.thingsproject.things.ui.RegisterNewItem
import br.com.thingsproject.things.utils.PermissionUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_itens.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.uiThread

open class ItensFragement : FragmentBasico(),
        SearchView.OnQueryTextListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected var itens = listOf<Item>()
    private lateinit var mCallback : OnNewSearch

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mGoogleApiClient : GoogleApiClient
    private lateinit var locRequest : LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura o googleApiClient
        mGoogleApiClient = GoogleApiClient.Builder(act)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        // permissoes
        PermissionUtils.validate(act, 0,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

        locRequest = LocationRequest.create()
        locRequest.interval = 10000
        locRequest.fastestInterval = 5000
        locRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(act)
    }

    //Infla a view do fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootview = inflater.inflate(R.layout.fragment_itens, container, false)
        val toolbar = rootview.findViewById<Toolbar>(R.id.toolbaR)
        if (toolbar != null) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }
        setHasOptionsMenu(true)
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.setHasFixedSize(true)
        // Tenho que resolver o problema da visualização do btn
        fab.setOnClickListener {
            activity?.startActivity<RegisterNewItem>()
        }
        // Talvez tenha que passar o task() AQUI
        tasks()
    }

    // Set do botão de pesquisa
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_bar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView  = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        /**
         * passa o retorno da pesquisa para o proximo fragment
         */
        doAsync {
            val itens = ItensService.getItensByName(query)
            uiThread {
                mCallback.OnsearchDone(itens)
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    /**
     * Interface
     * Aqui eu implemento a interface que deve passar os dados List<Item>
     */
    interface OnNewSearch {
        fun OnsearchDone(item: List<Item>)
    }

    /**
     * onAttach...
     * assegura a implementação da Interface
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mCallback = context as OnNewSearch
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString()
                    + " must implement OnNewSearch")
        }
    }

    override fun onPause() {
        super.onPause()
        if (mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient.connect()
        //tasks()
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locRequest, locationCallback, Looper.myLooper())
        Log.d(TAG, "onConnected")
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            handleNewLocation(location)
        }
    }

    private fun handleNewLocation(location : Location) {
        //val latLog = LatLng(location.latitude, location.longitude)
        Log.i("Location :: ", "Lat/Lng: ${location.latitude}/${location.longitude}")
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i("GoogleAPI", "Conexão interrompida")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("GoogleAPI", "Erro ao conectar: ${p0}")
    }

    //busca os itens e posibilita os clicks nos cards
    open fun tasks() {
        object: Thread() {
            override fun run() {
                val response = ItensService.getItens()
                activity?.runOnUiThread(Runnable {
                    recyclerView.adapter = CardAdapter(response.docs) { onClickCard(it)}
                })
            }
        }.start()
    }

    open fun onClickCard(unidade: Item) {
        //click no card abre a Tela Mais Detalhes
        activity?.startActivity<MaisInformacoes>("item" to unidade)
    }

    private fun hasPermission(permission: String) : Boolean {
        return ContextCompat.checkSelfPermission(act, permission) == PackageManager.PERMISSION_GRANTED
    }
}