package com.ufpb.getha.ui.catalogo.entries

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ufpb.getha.IP
import com.ufpb.getha.R
import com.ufpb.getha.ui.catalogo.Organismo
import com.ufpb.getha.ui.catalogo.mainColor
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

@Composable
@Preview
fun ZoonoseCatalog(
    zoonose: ZoonoseEntryJSON = ZoonoseEntryJSON(
        "1",
        "Zoonose",
        "Zoonose Científica",
        "Descrição da zoonose",
        Organismo.Virus.string,
        listOf("Agente 1", "Agente 2"),
        listOf("Vetor 1", "Vetor 2"),
        listOf("Transmissão 1", "Transmissão 2"),
        listOf("Sintoma 1", "Sintoma 2")
    )
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .height(250.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Canvas(
                    modifier = Modifier
                        .size(height = 200.dp, width = 50.dp)
                        .padding(start = 20.dp),
                    onDraw = {
                        drawRect(
                            color = mainColor,
                            size = size
                        )
                    }
                )
                Canvas(
                    modifier = Modifier
                        .size(
                            width = 50.dp,
                            height = 20.dp
                        )
                        .padding(start = 20.dp)
                        .rotate(180f)
                ) {
                    val width = size.width
                    val height = size.height

                    val path = Path().apply {
                        moveTo(width / 2, 0f)
                        lineTo(0f, height)
                        lineTo(width, height)
                        close()
                    }

                    drawPath(
                        path = path,
                        color = mainColor,
                        style = Fill
                    )
                }

            }
            Column(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        top = 20.dp,
                        end = 20.dp,
                    )
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = zoonose.nome)
                Text(text = zoonose.nomeCientifico)
            }
            Icon(
                painter = painterResource(id = R.drawable.virus),
                contentDescription = null,
                modifier = Modifier
                    .width(300.dp)
                    .padding(20.dp)
                    .align(Alignment.CenterVertically),
                tint = mainColor
            )
        }
    }
}