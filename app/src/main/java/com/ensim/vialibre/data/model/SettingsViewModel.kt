package com.ensim.vialibre.data.model
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ensim.vialibre.data.repository.SettingsRepository
import com.ensim.vialibre.ui.accessibility.FontSizeScale
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    val fontSizeScale: StateFlow<FontSizeScale> = repository.fontSizeScale
        .stateIn(viewModelScope, SharingStarted.Eagerly, FontSizeScale.MEDIUM)

    fun updateFontSize(scale: FontSizeScale) {
        viewModelScope.launch {
            repository.saveFontSize(scale)
        }
    }
}
