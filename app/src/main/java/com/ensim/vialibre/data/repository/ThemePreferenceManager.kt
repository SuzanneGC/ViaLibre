package com.ensim.vialibre.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ensim.vialibre.data.dataStore
import com.ensim.vialibre.ui.accessibility.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ThemePreferenceManager {
    //private val Context.dataStore by preferencesDataStore(name = "settings")
    private val THEME_KEY = stringPreferencesKey("theme_preference")

    suspend fun saveTheme(context: Context, theme: AppTheme) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.name
        }
    }

    fun getTheme(context: Context): Flow<AppTheme> {
        return context.dataStore.data
            .map { prefs ->
                val theme = prefs[THEME_KEY] ?: AppTheme.SYSTEM.name
                AppTheme.valueOf(theme)
            }
    }
}
