package br.com.thingsproject.things.ui

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import br.com.thingsproject.things.R
import br.com.thingsproject.things.dataClasses.Item
import br.com.thingsproject.things.domain.Dispo
import br.com.thingsproject.things.domain.ItensService
import br.com.thingsproject.things.domain.ItensService.delete
import br.com.thingsproject.things.domain.ItensService.putItens
import br.com.thingsproject.things.domain.TempoCusto
import br.com.thingsproject.things.extensions.setupToolbar
import br.com.thingsproject.things.extensions.string
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_register_new_item.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class EditItem : RegisterNewItem() {
    /*** falta colocar de qual activity essa e filha*/
    private val unidade : Item by lazy { intent.getParcelableExtra<Item>("item") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar(R.id.toolbarRI, "Editar e deletar", true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == R.id.excluirBtn) {
            // abre a janela de dialogo para confirmar a exclusão
            AlertDialog.Builder(this)
                    .setTitle("Remover item")
                    .setMessage(R.string.quest_delete)
                    .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialogInterface, i ->
                        doAsync {
                            val response = delete(unidade._id, token)
                            uiThread {
                                toast(response.message)
                            }
                        }
                    })
                    .setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialogInterface, i ->
                        toast(R.string.mantido)
                    })
                    .show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun buttomSalvar() {
        doAsync {
            val i = unidade
            //nome
            i.name = ItemName.string
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
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@EditItem)
            // Verifica permissões
            if (ActivityCompat.checkSelfPermission(this@EditItem, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@EditItem, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                        toast(R.string.location_erro)
                    }
            // descrição
            i.description = sugestions.text.toString()
            // salvar a alteração
            val response = putItens(unidade._id, i, token)
            uiThread {
                toast(response.message)
                finish()
            }
        }
    }
}