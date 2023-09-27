package com.groupec.githubfetchercompose.data.database.dao

import androidx.room.*
import com.groupec.githubfetchercompose.data.network.bodies.results.Repository

@Dao
interface RepositoryDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repository: com.groupec.githubfetchercompose.data.database.entities.Repository)

    @Query("SELECT * FROM repository")
    fun getAllRepositories(): List<Repository>
}