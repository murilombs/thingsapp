package br.com.thingsproject.things.utils

import android.content.Context
import android.widget.Toast
import br.com.thingsproject.things.R

class AccesValidator {
    var erros = ArrayList<Int>()

    fun ValidationContract(): String {
        return erros.toString()
    }

    fun isRequire(value: String, context: Context) {
        if (value.isEmpty() || value.length <= 0) {
            Toast.makeText(context, R.string.erro_campo_req, Toast.LENGTH_LONG).show()
            erros.add(R.string.erro_campo_req)
        }
    }

    fun hasMinlen(value: String, min: Int,context: Context) {
        if (value.isEmpty() || value.length < min) {
            Toast.makeText(context, R.string.len_req, Toast.LENGTH_LONG).show()
            erros.add(R.string.len_req)
        }
    }

    fun isEmail(value: String, context: Context) {
        val reg = Regex("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
        if (value.isEmpty() || reg.matches(value).not()) {
            Toast.makeText(context, R.string.email_req, Toast.LENGTH_LONG).show()
            erros.add(R.string.email_req)
        }
    }

    fun isValid(): Boolean {
        return erros.size == 0
    }
}