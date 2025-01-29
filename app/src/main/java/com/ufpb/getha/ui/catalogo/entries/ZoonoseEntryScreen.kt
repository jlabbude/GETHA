package com.ufpb.getha.ui.catalogo.entries

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.ufpb.getha.IP
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ZoonoseEntryJSON(
    val id: String,
    val nome: String,
    @SerialName("nome_cientifico")
    val nomeCientifico: String,
    val descricao: String,
    val organismo: String,
    val agentes: List<String>,
    val vetores: List<String>,
    val transmissoes: List<String>,
    val sintomas: List<String>,
)

@Composable
fun ZoonoseEntryScreen(zoonoseID: String) {
    val zoonose = remember { mutableStateOf<ZoonoseEntryJSON?>(null) }
    LaunchedEffect(Unit) {
        zoonose.value = Json.decodeFromString<ZoonoseEntryJSON>(
            HttpClient(Android).get(
                "http://$IP/get_zoonose_full?id=$zoonoseID"
            ).bodyAsText()
        )
    }
}