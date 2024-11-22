package com.example.flickrsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val api = FlickrApi.create()

    private val _images = MutableStateFlow<List<ImageItem>>(emptyList())
    val images: StateFlow<List<ImageItem>> = _images

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun searchImages(query: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = api.searchImages(query)
                _images.value = response.items
            } catch (e: Exception) {
                _images.value = emptyList() // Handle errors gracefully
            } finally {
                _loading.value = false
            }
        }
    }
}
