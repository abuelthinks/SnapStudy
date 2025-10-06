package com.kiarah.snapstudy

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class StudyResult(
    val imageUri: Uri,
    val extractedText: String,
    val aiResponse: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class UserMode { STUDENT, PARENT }

class SnapStudyViewModel : ViewModel() {
    private val _userMode = mutableStateOf(UserMode.STUDENT)
    val userMode: State<UserMode> = _userMode

    private val _currentImageUri = mutableStateOf<Uri?>(null)
    val currentImageUri: State<Uri?> = _currentImageUri

    private val _extractedText = mutableStateOf("")
    val extractedText: State<String> = _extractedText

    private val _aiResponse = mutableStateOf("")
    val aiResponse: State<String> = _aiResponse

    private val _isProcessing = mutableStateOf(false)
    val isProcessing: State<Boolean> = _isProcessing

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _history = mutableStateOf<List<StudyResult>>(emptyList())
    val history: State<List<StudyResult>> = _history

    private val apiService = ApiService.create()
    private val textRecognition = TextRecognitionUtil()

    fun toggleUserMode() {
        _userMode.value = if (_userMode.value == UserMode.STUDENT) UserMode.PARENT else UserMode.STUDENT
    }

    fun setImageUri(context: Context, uri: Uri) {
        _currentImageUri.value = uri
        processImage(context, uri)
    }

    private fun processImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            _isProcessing.value = true
            _error.value = null

            try {
                // Extract text from image
                val text = textRecognition.extractText(context, uri)
                _extractedText.value = text

                if (text.isNotEmpty()) {
                    // Get AI response
                    val response = apiService.getHomeworkHelp(text, _userMode.value)
                    _aiResponse.value = response

                    // Add to history
                    val result = StudyResult(uri, text, response)
                    _history.value = listOf(result) + _history.value.take(9)
                } else {
                    _error.value = "No text found in image. Try again with better lighting."
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun reset() {
        _currentImageUri.value = null
        _extractedText.value = ""
        _aiResponse.value = ""
        _error.value = null
        _isProcessing.value = false
    }
}
