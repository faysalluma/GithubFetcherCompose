package com.groupec.githubfetchercompose.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.groupec.githubfetchercompose.data.database.dao.RepositoryDao
import com.groupec.githubfetchercompose.data.database.entities.Repository

@Database(
    entities = [Repository::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract val repositoryDao: RepositoryDao

    companion object {
        private const val DATABASE_NAME = "SampleDB.sqlite"
        fun buildDatabase(context: Context?): AppDataBase {
            return Room.databaseBuilder(context!!, AppDataBase::class.java,
                DATABASE_NAME
            )
                .allowMainThreadQueries()
                .build()
        }
    }
}