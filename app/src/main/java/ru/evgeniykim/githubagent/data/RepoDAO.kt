package ru.evgeniykim.githubagent.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RepoDAO {
    @Insert
    suspend fun insert(repo: Repos)

    @Update
    suspend fun update(repo: Repos)

    @Delete
    suspend fun delete(repo: Repos)

    @Query("SELECT * FROM repos")
    fun getAllRepos(): LiveData<List<Repos>>
}