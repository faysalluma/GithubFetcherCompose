package com.groupec.githubfetchercompose.data.datastore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext val context: Context) {
    companion object {
        private val REPO_NAME_KEY = stringPreferencesKey("reponame")
        private val USER_NAME_KEY = stringPreferencesKey("username")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    val fullNameFlow: Flow<Pair<String,String>> = context.dataStore.data
        .map { preferences ->
            // No type safety.
            Pair(
                preferences[REPO_NAME_KEY] ?: "",
                preferences[USER_NAME_KEY] ?: "",
            )
        }

    suspend fun setFullName(reponame: String, username: String) {
        context.dataStore.edit { datastore ->
            datastore[REPO_NAME_KEY] = reponame
            datastore[USER_NAME_KEY] = username
        }
    }
}