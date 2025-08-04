package com.ufpb.getha.ui.aparelhos

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufpb.getha.IP
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.ConnectException


@Serializable
data class AparelhoJSON(
    @SerialName("id") val id: String,
    @SerialName("nome") val nome: String,
)

class AparelhosViewModel : ViewModel() {
    private val client = HttpClient(Android) {
        install(HttpTimeout) {
            requestTimeoutMillis = 500
            connectTimeoutMillis = 500
            socketTimeoutMillis = 500
        }
    }

    private val _imagesMap = MutableStateFlow<LinkedHashMap<AparelhoJSON, Bitmap>>(linkedMapOf())
    val imagesMap: StateFlow<LinkedHashMap<AparelhoJSON, Bitmap>> = _imagesMap

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val images = mutableMapOf<AparelhoJSON, Bitmap>()
                val res = client.get("http://$IP/serve_aparelhos").bodyAsText()
                val aparelhos = Json.decodeFromString<List<AparelhoJSON>>(res)
                for (aparelho in aparelhos) {
                    val byteArray =
                        client.get("http://$IP/serve_image?ID=${aparelho.id}").readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                        ?: BitmapFactory.decodeResource(
                            Resources.getSystem(),
                            android.R.drawable.ic_menu_camera
                        )
                    images.put(aparelho, bitmap)
                }
                _imagesMap.value = images as LinkedHashMap<AparelhoJSON, Bitmap>
            } catch (_: ConnectException) {
                _imagesMap.value = linkedMapOf()
            } catch (_: HttpRequestTimeoutException) {
                _imagesMap.value = linkedMapOf()
            } catch (_: Exception) {
                _imagesMap.value = linkedMapOf()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
