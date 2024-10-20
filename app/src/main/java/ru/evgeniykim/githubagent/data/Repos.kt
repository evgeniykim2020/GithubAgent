package ru.evgeniykim.githubagent.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class Repos(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userName: String,
    val repoName: String
)
