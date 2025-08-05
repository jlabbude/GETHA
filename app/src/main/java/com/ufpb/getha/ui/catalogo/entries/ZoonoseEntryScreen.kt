package com.ufpb.getha.ui.catalogo.entries

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val nome: String,
    @SerialName("nome_cientifico")
    val nomeCientifico: String,
    val organismo: String,
    val agentes: List<String>,
    val transmissoes: List<String>,
    val vetores: List<String>,
    val regioes: List<String>,
    val profilaxias: List<String>,
    val diagnosticos: List<String>,
    val descricao: String,
)

@Composable
fun ZoonoseEntryScreen(zoonoseID: String) {
    val zoonose = remember { mutableStateOf<ZoonoseEntryJSON?>(null) }
    LaunchedEffect(Unit) {
        zoonose.value = Json.decodeFromString<ZoonoseEntryJSON>(
            HttpClient(Android).get(
                "http://$IP/get_zoonose_full?ID=$zoonoseID"
            ).bodyAsText()
        )
    }
    if (zoonose.value != null) {
        @Suppress("USELESS_CAST")
        ZoonoseCatalog(
            zoonose = zoonose.value!!,
        )
    }
}

@Composable
@Preview
fun ZoonoseCatalog(
    zoonose: ZoonoseEntryJSON = ZoonoseEntryJSON(
        "Rabies",
        "Rabies lyssavirus",
        Organismo.Virus.string,
        listOf("Agente1", "agente2"),
        listOf("Transmissão1", "transmissão2"),
        listOf("Vetor1", "vetor2"),
        listOf(
            "Regiao1",
            "regiao2",
            "regiao2",
            "regiao2",
            "regiao2",
            "regiao2",
            "regiao2",
            "regiao2"
        ),
        listOf("Profilaxia1", "profilaxia2"),
        listOf("Diagonostico1", "diagonostico2"),
        "Descrição da zoonose",
    ),
) {
    var descOpen = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
    ) {
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
                    Column {
                        Canvas(
                            modifier = Modifier
                                .size(height = 230.dp, width = 50.dp)
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
                Box {
                    if (Build.VERSION.SDK_INT >= 31) {
                        Organismo.fromString(zoonose.organismo).toComponent()(
                            Modifier
                                .size(200.dp)
                                .padding(10.dp)
                                .align(Alignment.Center)
                                .offset(x = (-1).dp, y = (1).dp)
                                .blur(10.dp)
                                .alpha(0.3f),
                        )
                    }
                    Organismo.fromString(zoonose.organismo).toComponent()(
                        Modifier
                            .size(230.dp)
                            .padding(10.dp)
                            .align(Alignment.Center),
                    )
                }
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp),
                onClick = {
                    descOpen.value = !descOpen.value
                },
            ) {
                Icon(
                    imageVector = if (descOpen.value) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = "Expandir descrição",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            if (descOpen.value) {
                Text(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "Descrição",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(color = Color(0x50000000), offset = Offset(3.0f, 3.0f)),
                        fontSize = 24.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp, start = 30.dp, end = 30.dp),
                    text = zoonose.descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp)
                    .padding(top = 10.dp)
            )
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
                ZoonoseInfoCell("Agentes", zoonose.agentes.joinToString())
                ZoonoseInfoCell("Transmissões", zoonose.transmissoes.joinToString())
                ZoonoseInfoCell("Vetores", zoonose.vetores.joinToString())
                ZoonoseInfoCell("Regiões de incidência", zoonose.regioes.joinToString())
                ZoonoseInfoCell("Profilaxia", zoonose.profilaxias.joinToString())
                ZoonoseInfoCell("Diagnosticos", zoonose.diagnosticos.joinToString())
            }
        }
    }
}

@Composable
fun ZoonoseInfoCell(title: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 0.dp,
                bottom = 15.dp,
                end = 20.dp
            )
    ) {
        Canvas(
            modifier = Modifier
                .size(width = 5.dp, height = 20.dp)
                .align(Alignment.CenterVertically)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(10.dp),
                    clip = true
                ),
            onDraw = {
                drawRoundRect(
                    color = Color(0xD0598462),
                    size = size,
                    cornerRadius = CornerRadius(x = size.height / 2, y = size.height / 2)
                )
            }
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 5.dp, end = 20.dp, top = (2.5).dp, bottom = (2.5).dp)
                    .fillMaxWidth()
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}