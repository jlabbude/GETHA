package com.ufpb.getha.ui.aparelhos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.IOException

class AparelhosViewModel : ViewModel() {
    private val mImageButton: MutableLiveData<Bitmap> = MutableLiveData()

    fun getBitmap(context : Context): Bitmap {

        try{
            val assetManager = context.assets
            val inputStream = assetManager.open("aparelhosfotos/aparelhofototeste.png")
            mImageButton.value = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }

        val bitmap = if (mImageButton.value != null) mImageButton.value else {
            BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_menu_camera)
        } // fallback shenanigans
        return bitmap!!
    }

    val imageButton: LiveData<Bitmap>
        get() = mImageButton
}