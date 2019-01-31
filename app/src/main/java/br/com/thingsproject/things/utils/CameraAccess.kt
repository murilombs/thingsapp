package br.com.thingsproject.things.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class CameraAccess {
    companion object {
        private val TAG = "camera"
    }

    //Arquivo pode ser nulo
    var file : File? = null
        private set

    //giro da tela recupera o estado
    fun init(icicle : Bundle?) {
        if (icicle != null) {
            file = icicle.getSerializable("file") as File
        }
    }

    //Salva o estado
    fun onSaveInstanceState(outState: Bundle) {
        if (file != null) {
            outState.putSerializable("file", file)
        }
    }

    //Intent abre a camera
    fun openCamera(context: Context, fileName: String): Intent {
        file = getSdCardFile(context, fileName)
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file?.apply {
            val uri = FileProvider.getUriForFile(context,
                    context.applicationContext.packageName + ".provider", this)
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        return i
    }

    fun getSdCardFile(context: Context, fileName: String): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if(!dir.exists()) {
            dir.mkdir()
        }
        return File(dir,fileName)
    }

    //Lê o bitmap no tamanho desejado
    fun getBitmap(w: Int, h: Int): Bitmap? {
        file?.apply {
            if (exists()) {
                Log.d(TAG, absolutePath)
                //redimensiona
                val bitmap = ImageUtils.resize(this, w, h)
                Log.d(TAG, "getBitMap w/h: " + bitmap.width + "/" + bitmap.height)
                return bitmap
            }
        }
        return null
    }

    //Salva o bitmap reduzido
    fun save(bitmap: Bitmap) {
        file?.apply {
            val out = FileOutputStream(this)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            Log.d(TAG, "Foto salva: " + absolutePath)
        }
    }
}