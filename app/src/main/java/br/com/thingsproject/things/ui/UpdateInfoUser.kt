package br.com.thingsproject.things.ui

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import br.com.thingsproject.things.R
import br.com.thingsproject.things.base.BaseActivity
import br.com.thingsproject.things.dataClasses.UpdateUser
import br.com.thingsproject.things.domain.ItensService
import br.com.thingsproject.things.domain.UserService
import br.com.thingsproject.things.domain.UserService.updateUser
import br.com.thingsproject.things.utils.CameraAccess
import br.com.thingsproject.things.utils.Prefs
import kotlinx.android.synthetic.main.update_infor_user.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class UpdateInfoUser : BaseActivity() {
    private val camera = CameraAccess()
    val user : UpdateUser? by lazy { intent.getParcelableExtra<UpdateUser>("user") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_infor_user)
        camera.init(savedInstanceState)
        initViews()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        camera.onSaveInstanceState(outState)
    }

    private fun initViews() {
        val first_nome = user_firstN.text
        val second_nome = user_secondN.text
        btn_AbrirCamera.setOnClickListener {
            onClickCamera()
        }
        btnUpdate.setOnClickListener {
            updateBtn(first_nome, second_nome)
        }
    }

    private fun onClickCamera() {
        //nome do arquivo da foto
        val fileName = "image_item.jpg"
        //Abre a camera
        val intente = camera.openCamera(this, fileName)
        startActivityForResult(intente, 0)
    }

    private fun updateBtn(first_nome : Editable, second_nome : Editable) {
        //val u : UpdateUser? = user //?
        val u = user?: UpdateUser()
        doAsync {
            val token = Prefs.getString("token") // recupera o token do cache
            val dados = UserService.getProfile(token) // usa o token para o link da img
            val id = dados._id
            // foto
            val file = camera.file
            if (file != null && file.exists()) {
                val r = ItensService.postFoto(file)
                u.profilePicture = r
            }
            u.first_name    = first_nome.toString().toLowerCase()
            u.second_name   = second_nome.toString().toLowerCase()
            val response = updateUser(id, token, u)
            if (response.status == "OK") {
                uiThread { Toast.makeText(context, "Dados Atualizados", Toast.LENGTH_LONG).show() }
            }
        }
    }
}