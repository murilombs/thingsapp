package br.com.thingsproject.things.domain.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import br.com.thingsproject.things.dataClasses.Profiles

@Dao
interface UserDAO {
    @Query("SELECT * FROM userProfile")
    fun findAll(): Profiles

    @Insert
    fun insert(user: Profiles)

    @Delete
    fun delete(user: Profiles)
}