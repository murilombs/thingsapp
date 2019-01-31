package br.com.thingsproject.things.domain.dao

import android.arch.persistence.room.Room
import br.com.thingsproject.things.Application

object DatabaseManager {
    // Singleton do Room: banco de dados
    private var dbInstance: UserDatabase

    init {
        val appContext = Application.getInstance().applicationContext

        // Configura o Room
        dbInstance = Room.databaseBuilder(
                appContext,
                UserDatabase::class.java,
                "userProfile.sqlite")
                .build()
    }

    fun getUserDAO(): UserDAO {
        return dbInstance.userDAO()
    }
}