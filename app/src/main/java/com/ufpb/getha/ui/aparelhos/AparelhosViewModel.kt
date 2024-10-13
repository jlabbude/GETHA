package com.ufpb.getha.ui.aparelhos
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.statement.readBytes

class AparelhosViewModel : ViewModel() {
    private val mImageButton: MutableLiveData<Bitmap> = MutableLiveData()

    private val client = HttpClient(Android) {
        install(HttpTimeout) {
            requestTimeoutMillis = 500
            connectTimeoutMillis = 500
            socketTimeoutMillis = 500
        }
    }

    suspend fun getBitmap(context: Context): Bitmap {
        return try {
            val byteArray = client.get("http://192.168.15.11:8000/aparelhos_image").readBytes()
            val decode = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            val bitmap = if ( decode == null) {
                BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_menu_camera)
            } else {
                decode
            }
            mImageButton.value = bitmap!!
            bitmap
        } catch (_: ConnectTimeoutException) {
            BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_menu_camera)
        } catch (_: HttpRequestTimeoutException) {
            BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_menu_camera)
        } catch (_: Exception) {
            BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_menu_camera)
        }
    }
}
