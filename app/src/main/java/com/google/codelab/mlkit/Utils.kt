package com.nicomahnic.covidcertificatescanner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.IOException
import java.io.InputStream

object Utils{
    fun getBitmapFromAsset(context: Context, filePath: String, side: Int): List<Bitmap>? {
        val assetManager = context.assets
        val inputStream: InputStream
        var bitmap: Bitmap? = null
        val bitmaps = mutableListOf<Bitmap>()
        try {
            inputStream = assetManager.open(filePath)

            bitmap = BitmapFactory.decodeStream(inputStream)
            Log.d("NM","Bitmap Width: ${bitmap.width} Height: ${bitmap.height} side: $side")
            bitmaps.add(bitmap)
            if(side == 2) {
                bitmaps.add(Bitmap.createBitmap(bitmap,370,240,600,40)) //Nombre vacuna1
                bitmaps.add(Bitmap.createBitmap(bitmap,1000,310,240,40)) //Fecha1
                bitmaps.add(Bitmap.createBitmap(bitmap,1000,385,175,40)) //Lote1
                bitmaps.add(Bitmap.createBitmap(bitmap,375,343,606,117)) //Lugar1

                bitmaps.add(Bitmap.createBitmap(bitmap,370,240+262,600,40)) //Nombre vacuna2
                bitmaps.add(Bitmap.createBitmap(bitmap,1000,310+262,240,40)) //Fecha2
                bitmaps.add(Bitmap.createBitmap(bitmap,1000,385+262,175,40)) //Lote2
                bitmaps.add(Bitmap.createBitmap(bitmap,375,570,606,117)) //Lugar2
            }else{
                bitmaps.add(Bitmap.createBitmap(bitmap,730,400,620,50)) //Nombre Persona
                bitmaps.add(Bitmap.createBitmap(bitmap,730,548,200,50)) //Documento
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmaps
    }
}