package com.ufpb.getha.ui.aparelhos
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
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
        val byteArray = client.get("http://192.168.15.9:8000/aparelhos_image").readBytes()
        val decode = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val bitmap = if ( decode == null) {
            BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_menu_camera)
        } else {
            decode
        }
        mImageButton.value = bitmap!!
        return bitmap
    }
}
