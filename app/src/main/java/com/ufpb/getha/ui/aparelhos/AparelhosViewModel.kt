package com.ufpb.getha.ui.aparelhos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.net.ConnectException

class AparelhosViewModel : ViewModel() {
    private val mImageButton: MutableLiveData<Bitmap> = MutableLiveData()

    suspend fun getBitmap(context : Context): Bitmap {

        try {
            val byteArray = HttpClient(Android).get("http://192.168.15.11:8000/aparelhos_image").readBytes()
            mImageButton.value = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: ConnectException) {
            return BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_menu_camera)
        }
        return mImageButton.value!!
    }

}