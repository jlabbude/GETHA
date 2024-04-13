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

    fun getBitmap(context : Context): Bitmap{

        try{
            val assetManager = context.assets
            val inputStream = assetManager.open("aparelhosfotos/banho-histologico-1.png")
            mImageButton.value = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }

        return mImageButton.value!!
    }

    val imageButton: LiveData<Bitmap>
        get() = mImageButton
}