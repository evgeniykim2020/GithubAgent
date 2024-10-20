package ru.evgeniykim.githubagent.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Repos::class], version = 1)
abstract class ReposDB : RoomDatabase() {
    abstract fun repoDao(): RepoDAO

    companion object {
        @Volatile
        private var INSTANCE: ReposDB? = null

        fun getDatabase(context: Context): ReposDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    ReposDB::class.java,
                    "repos_database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}