package com.ufpb.getha.ui.aparelhos
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AparelhosViewModel : ViewModel() {
    private val client = HttpClient(Android) {
        install(HttpTimeout) {
            requestTimeoutMillis = 500
            connectTimeoutMillis = 500
            socketTimeoutMillis = 500
        }
    }

    private val _imagesMap = MutableStateFlow<LinkedHashMap<Int, Bitmap>>(linkedMapOf())
    val imagesMap: StateFlow<LinkedHashMap<Int, Bitmap>> = _imagesMap

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val images = mutableMapOf<Int, Bitmap>()
                for (i in 1..5) {
                    val byteArray = client.get("http://192.168.15.12:8000/aparelhos_image").readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                        ?: BitmapFactory.decodeResource(Resources.getSystem(), android.R.drawable.ic_menu_camera)
                    images.put(i, bitmap)
                }
                _imagesMap.value = images as LinkedHashMap<Int, Bitmap>
            } catch (_: Exception) {
                _imagesMap.value = linkedMapOf()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
