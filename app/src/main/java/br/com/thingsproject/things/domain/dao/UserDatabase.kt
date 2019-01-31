package br.com.thingsproject.things.domain.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import br.com.thingsproject.things.dataClasses.Profiles

@Database(entities = arrayOf(Profiles::class), version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDAO() : UserDAO
}