package com.groupec.githubfetchercompose.data.dto

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContributorDTO(val login: String, val avatar_url: String) : Parcelable {
    companion object NavigationType : NavType<ContributorDTO>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ContributorDTO? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): ContributorDTO {
            return Gson().fromJson(value, ContributorDTO::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: ContributorDTO) {
            bundle.putParcelable(key, value)
        }
    }
}
