package br.com.thingsproject.things.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.BaseActivity
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.domain.Dispo
import br.com.thingsproject.things.domain.ItensService
import br.com.thingsproject.things.domain.ItensService.save
import br.com.thingsproject.things.domain.TempoCusto
import br.com.thingsproject.things.extensions.isEmpty
import br.com.thingsproject.things.extensions.setupToolbar
import br.com.thingsproject.things.extensions.string
import br.com.thingsproject.things.utils.CameraAccess
import br.com.thingsproject.things.utils.PermissionUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_register_new_item.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

open class RegisterNewItem : BaseActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected val camera = CameraAccess()
    protected val item : Item by lazy { intent.getParcelableExtra<Item>("item") } //?
    protected lateinit var fusedLocationClient: FusedLocationProviderClient
    protected lateinit var mGoogleApiClient : GoogleApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new_item)
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
        setupToolbar(R.id.toolbarRI, "Novo item", true)
        inicializa() // inicializa as views
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Salva o estado do arquivo caso gire a tela
        camera.onSaveInstanceState(outState)
    }

    // Gerencia a resposta de permissão
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (result in grantResults){
            if (result == PackageManager.PERMISSION_DENIED) {
                alertAndFinish()
                return
            }
        }
    }

    open fun alertAndFinish() {
        run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name).setMessage("Para utilizar este aplicativo, você precisa aceitar as permissões.")
            // Add the buttons
            builder.setPositiveButton("OK") { dialog, id -> finish() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    //inicia as views
    private fun inicializa() {
        item?.apply {
            //nome
            ItemName.string = name
            //tempo/custo
            when (timeCust) {
                "12 Horas | R$ 15" -> tempoCusto.check(R.id.doze)
                "20 Horas | R$ 17" -> tempoCusto.check(R.id.vinte)
                "40 Horas | R$ 20" -> tempoCusto.check(R.id.quarenta)
            }
            //disponibilidade
            when (delivery) {
                "Disponivel"   -> disponibilidade.check(R.id.disponivel)
                "Indisponivel" -> disponibilidade.check(R.id.indisponivel)
            }
        }
        // salvar
        btnRegistrarItem.setOnClickListener { buttomSalvar() }
        // abrir camera
        bAbrirCamera.setOnClickListener { onClickCamera() }
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    override fun onStop() {
        mGoogleApiClient.disconnect()
        super.onStop()
    }

    override fun onConnected(p0: Bundle?) {
        Log.d("GoogleAPI", "Conexão estabelecida")
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d("GoogleAPI", "Conexão interrompida")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("GoogleAPI", "Erro ao conectar: ${p0}")
    }

    open fun buttomSalvar() {
        if (ItemName.isEmpty()) {
            ItemName.error = getString(R.string.erro_name_registerNewItem)
            sugestions.error = getString(R.string.erro_campo_req)
            return
        }
        doAsync {
            //Cria um Item
            val i = item
            //nome
            i.name = ItemName.string.toLowerCase()
            //tempo/custo
            i.timeCust = when (tempoCusto.checkedRadioButtonId) {
                R.id.doze -> TempoCusto.twelve.string
                R.id.vinte -> TempoCusto.twenty.string
                else -> TempoCusto.forty.string
            }
            //disponibilidade
            i.delivery = when (disponibilidade.checkedRadioButtonId) {
                R.id.disponivel -> Dispo.dis.string
                else -> Dispo.indis.string
            }
            //Imagens
            val file = camera.file
            if (file != null && file.exists()){
                val r = ItensService.postFoto(file)
                i.itensImages = r
            }
            // Localização
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@RegisterNewItem)
            // Verifica permissões
            if (ActivityCompat.checkSelfPermission(this@RegisterNewItem, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@RegisterNewItem, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                alertAndFinish()
                //return
            }
            // Fused Location Provider API
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        i.latitude = location.latitude.toString()
                        i.longitude = location.longitude.toString()
                    }
                    .addOnFailureListener {
                        Log.d("Localização", "Não foi possível ao buscar a localização do GPS")
                    }
            // descrição
            i.description = sugestions.text.toString().toLowerCase()
            //salvar do db
            val response = save(i)
            uiThread {
                toast(response.message)
                finish()
            }
        }
    }

    private fun onClickCamera() {
        val ms = System.currentTimeMillis()
        //nome do arquivo da foto
        val fileName = if (item != null)
            "image_item.jpg" else "image_item${ms}"
        //Abre a camera
        val intente = camera.openCamera(this, fileName)
        startActivityForResult(intente, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //aqui fica o redimensionamento da imagem
            val bitmap = camera.getBitmap(600, 600)
            if (bitmap != null) {
                //salva o arquivo nesse tamanho
                camera.save(bitmap)
                //tbm mostra a foto pronta
            }
        }
    }

    private fun toast(s: String) {
        Toast.makeText(baseContext, s , Toast.LENGTH_SHORT).show()
    }
}
