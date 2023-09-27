package com.groupec.githubfetchercompose.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repository")
class Repository(
    @field:ColumnInfo(name = "id") @field:PrimaryKey var id: Long,
    @field:ColumnInfo(name = "full_name") var full_name: String,
    @field:ColumnInfo(name = "description") var description: String,
    @field:ColumnInfo(name = "languages_url") var languages_url: String,
    @field:ColumnInfo(name = "stargazers_url") var stargazers_url: String,
    @field:ColumnInfo(name = "branches_url") var branches_url: String,
    @field:ColumnInfo(name = "contributors_url") var contributors_url: String,
)