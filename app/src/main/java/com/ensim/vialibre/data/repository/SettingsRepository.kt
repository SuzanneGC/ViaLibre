package com.ensim.vialibre.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ensim.vialibre.data.dataStore
import com.ensim.vialibre.ui.accessibility.FontSizeScale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property pour accéder à DataStore
//val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {
    companion object {
        private val FONT_SIZE_KEY = stringPreferencesKey("font_size")
    }

    val fontSizeScale: Flow<FontSizeScale> = context.dataStore.data
        .map { preferences ->
            val saved = preferences[FONT_SIZE_KEY] ?: FontSizeScale.MEDIUM.name
            FontSizeScale.valueOf(saved)
        }


    suspend fun saveFontSize(size: FontSizeScale) {
        context.dataStore.edit { preferences ->
            preferences[FONT_SIZE_KEY] = size.name
        }
    }
}
