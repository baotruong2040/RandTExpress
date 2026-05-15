package com.example.randtexpress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randtexpress.data.preferences.SessionData
import com.example.randtexpress.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    // The app root reads this once and switches between auth/home based on token presence.
    val sessionData: StateFlow<SessionData> = userPreferences.sessionData.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SessionData()
    )

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearSession()
        }
    }
}
