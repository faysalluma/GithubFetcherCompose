package com.groupec.githubfetchercompose.data.dto

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepositoryDTO(val id: Int, val full_name: String, val description: String?, val languages_url: String,
                         val stargazers_url: String, val branches_url : String, val contributors_url: String) : Parcelable {
    companion object NavigationType : NavType<RepositoryDTO>(isNullableAllowed = true) {
        override fun get(bundle: Bundle, key: String): RepositoryDTO? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): RepositoryDTO {
            return Gson().fromJson(value, RepositoryDTO::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: RepositoryDTO) {
            bundle.putParcelable(key, value)
        }
    }
}
