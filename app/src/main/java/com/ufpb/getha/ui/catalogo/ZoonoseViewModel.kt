package com.ufpb.getha.ui.catalogo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufpb.getha.IP
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.ConnectException

@Serializable
enum class Organismo(val string: String) {
    Virus("Virus"),
    Bacteria("Bacteria"),
    Protozoario("Protozoario"),
    Fungo("Fungo"),
    Helminto("Helminto");

    fun toComponent(): OrganismoComponent {
        return when (this) {
            Virus -> VIRUS
            Bacteria -> BACTERIA
            Protozoario -> PROTOZOARIO
            Fungo -> FUNGO
            Helminto -> HELMINTO
        }
    }

    companion object {
        fun fromString(value: String): Organismo {
            return entries.first { it.string == value }
        }
    }
}

@Serializable
data class ZoonoseCardJSON(
    @SerialName("id") val id: String,
    @SerialName("nome") val nome: String,
    @SerialName("nome_cientifico") val nomeCientifico: String,
    @SerialName("organismo") val organismo: String
)

class ZoonoseViewModel : ViewModel() {

    private val client = HttpClient(Android) {
        install(HttpTimeout) {
            requestTimeoutMillis = 500
            connectTimeoutMillis = 500
            socketTimeoutMillis = 500
        }
    }

    private val _zoonoses = MutableStateFlow<List<ZoonoseCardJSON>>(emptyList())
    val zoonoses: StateFlow<List<ZoonoseCardJSON>> = _zoonoses

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchZoonose()
    }

    private fun fetchZoonose() {
        viewModelScope.launch {
            try {
                val zoonosesRes = client.get("http://$IP/serve_zoonoses").bodyAsText()
                _zoonoses.value = Json.decodeFromString<List<ZoonoseCardJSON>>(zoonosesRes)
            } catch (e: ConnectException) {
                Log.e("ZoonoseViewModel", "Connection error", e)
                _zoonoses.value = emptyList()
            } catch (e: HttpRequestTimeoutException) {
                Log.e("ZoonoseViewModel", "Timeout error", e)
                _zoonoses.value = emptyList()
            } catch (e: Exception) {
                Log.e("ZoonoseViewModel", "Error fetching zoonoses", e)
                _zoonoses.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}