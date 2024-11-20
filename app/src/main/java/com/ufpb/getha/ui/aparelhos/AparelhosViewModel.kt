package com.ufpb.getha.ui.aparelhos

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import io.ktor.client.statement.readText
import io.ktor.utils.io.reader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.ConnectException


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
                val res = client.get("http://192.168.15.12:8000/aparelhos_ids").bodyAsText()
                val ids = Json.decodeFromString<List<Int>>(
                    res
                )
                for (id in ids) {
                    val byteArray =
                        client.get("http://192.168.15.12:8000/aparelhos_image?id=$id").readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                        ?: BitmapFactory.decodeResource(
                            Resources.getSystem(),
                            android.R.drawable.ic_menu_camera
                        )
                    images.put(id, bitmap)
                }
                _imagesMap.value = images as LinkedHashMap<Int, Bitmap>
            } catch (_: ConnectException) {
                _imagesMap.value = linkedMapOf()
            } catch (_: HttpRequestTimeoutException) {
                _imagesMap.value = linkedMapOf()
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}
