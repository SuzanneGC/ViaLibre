package com.ensim.vialibre.data.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ensim.vialibre.ui.accessibility.AppTheme
import com.ensim.vialibre.data.repository.ThemePreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val _theme = MutableStateFlow(AppTheme.SYSTEM)
    val theme: StateFlow<AppTheme> = _theme

    init {
        viewModelScope.launch {
            ThemePreferenceManager.getTheme(application).collect {
                _theme.value = it
            }
        }
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            ThemePreferenceManager.saveTheme(getApplication(), theme)
        }
    }
}
