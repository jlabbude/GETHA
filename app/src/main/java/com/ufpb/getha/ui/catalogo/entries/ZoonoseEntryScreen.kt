package com.ufpb.getha.ui.catalogo.entries

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ufpb.getha.IP
import com.ufpb.getha.ui.catalogo.Organismo
import com.ufpb.getha.ui.catalogo.mainColor
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream

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
    val profilaxia: List<String>,
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
        "Rabies",
        "Rabies lyssavirus",
        "Descrição da zoonose",
        Organismo.Virus.string,
        listOf("Agente1", "agente2"),
        listOf("Vetor1", "vetor2"),
        listOf("Transmissão1", "transmissão2"),
        listOf("Profilaxia1", "profilaxia2"),
        listOf("Sintoma1", "sintoma2")
    )
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Canvas(
            modifier = Modifier
                .width(width = 50.dp)
                .fillMaxHeight()
                .padding(start = 20.dp),
            onDraw = {
                drawRect(
                    color = mainColor,
                    size = size
                )
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
            ) {
                Column {
                   /*Canvas(
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
                    }*/
                }
                Column(
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            top = 20.dp,
                            end = 20.dp,
                        )
                        .size(80.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = zoonose.nome,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = zoonose.nomeCientifico,
                        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                var bitmap by remember { mutableStateOf<Bitmap?>(null) }
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    val assetManager = context.assets
                    val inputStream: InputStream = assetManager.open("rabies.jpg")
                    bitmap = BitmapFactory.decodeStream(inputStream)
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "lyssa",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(230.dp)
                            .padding(10.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(25.dp),
                                clip = true
                            )
                            .clip(RoundedCornerShape(25.dp))
                            .align(Alignment.CenterVertically),
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 50.dp,
                        top = 36.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    )
            ) {
                ZoonoseInfoCell("Vetores", zoonose.vetores.joinToString())
                ZoonoseInfoCell("Agentes", zoonose.agentes.joinToString())
                ZoonoseInfoCell("Transmissões", zoonose.transmissoes.joinToString())
                ZoonoseInfoCell("Profilaxia", zoonose.profilaxia.joinToString())
                ZoonoseInfoCell("Sintomas", zoonose.sintomas.joinToString())
                HorizontalDivider(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(300.dp)
                        .padding(top = 30.dp)
                )
            }
        }
    }
}

@Composable
fun ZoonoseInfoCell(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 10.dp,
                bottom = 20.dp,
                end = 20.dp
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}