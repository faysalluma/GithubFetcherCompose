package com.groupec.githubfetchercompose.data.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepositoryDTO(val id: Int, val full_name: String, val description: String?, val languages_url: String,
                         val stargazers_url: String, val branches_url : String, val contributors_url: String) : Parcelable
