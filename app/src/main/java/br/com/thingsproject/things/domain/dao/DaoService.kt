package br.com.thingsproject.things.domain.dao

import br.com.thingsproject.things.dataClasses.Profiles

object DaoService {
    // Salva no Banco de Dados interno
    fun salvarIntern(profile: Profiles): Boolean {
        val dao = DatabaseManager.getUserDAO()
        dao.insert(profile)
        return true
    }

    //Recupera os dados salvos
    fun getIntern(): Profiles {
        val dao = DatabaseManager.getUserDAO()
        val user = dao.findAll()
        return user
    }
}