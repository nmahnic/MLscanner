package com.nicomahnic.covidcertificatescanner

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.nicomahnic.covidcertificatescanner.Utils.getBitmapFromAsset
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity(), OnItemSelectedListener {
    private lateinit var mImageView: ImageView
    private lateinit var mTextButton: Button
    private lateinit var mSelectedImage: List<Bitmap>
    private lateinit var tvVaccineBatch: TextView
    private lateinit var tvVaccineDate: TextView
    private lateinit var tvVaccineName: TextView
    private lateinit var tvVaccinePlace: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mImageView = findViewById(R.id.image_view)
        mTextButton = findViewById(R.id.button_text)
        tvVaccineBatch = findViewById(R.id.tv_vaccine_batch)
        tvVaccineDate = findViewById(R.id.tv_vaccine_date)
        tvVaccineName = findViewById(R.id.tv_vaccine_name)
        tvVaccinePlace = findViewById(R.id.tv_vaccine_place)

        mTextButton.setOnClickListener { runTextRecognition() }



        val dropdown = findViewById<Spinner>(R.id.spinner)
        val items = arrayOf("Frente Carnet 1", "Dorso Carnet 1", "Frente Carnet 2", "Dorso Carnet 2")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter
        dropdown.onItemSelectedListener = this
    }

    private fun runTextRecognition() {
        val images = mSelectedImage.subList(1,mSelectedImage.size)
        images.forEachIndexed { index, bitmap ->
            val image = InputImage.fromBitmap(bitmap, 0)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            mTextButton.isEnabled = false
            recognizer.process(image)
                    .addOnSuccessListener { texts ->
                        processTextRecognitionResult(texts, index)
                    }
                    .addOnFailureListener { e -> // Task failed with an exception
                        e.printStackTrace()
                    }
        }
        mTextButton.isEnabled = true
    }

    private fun processTextRecognitionResult(texts: Text, index: Int) {
        var linea = ""
        texts.textBlocks.forEach { linea += it.text }
        linea = linea.replace('\n', ' ')
        Log.d("NM", "Text $index -> $linea")
//        tvVaccineBatch.text = texts.textBlocks[7].text
//        tvVaccineDate.text = texts.textBlocks[3].text
//        tvVaccineName.text = texts.textBlocks[1].text
//        tvVaccinePlace.text = texts.textBlocks[5].text
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, v: View, position: Int, id: Long) {
        when (position) {
            0 -> mSelectedImage = getBitmapFromAsset(this, "carnet_vacunacion_1.gif",1)!!
            1 -> mSelectedImage = getBitmapFromAsset(this, "carnet_vacunacion_2.gif",2)!!
            2 -> mSelectedImage = getBitmapFromAsset(this, "carnet_vacunacion_3.gif",1)!!
            3 -> mSelectedImage = getBitmapFromAsset(this, "carnet_vacunacion_4.gif",2)!!
        }

        val scaleFactor = (mSelectedImage[0].width.toFloat() / mSelectedImage[0].width.toFloat()).coerceAtLeast(mSelectedImage[0].height.toFloat() / mSelectedImage[0].height.toFloat())
        val resizedBitmap = Bitmap.createScaledBitmap(
                mSelectedImage[0],
                (mSelectedImage[0].width / scaleFactor).toInt(),
                (mSelectedImage[0].height / scaleFactor).toInt(),
                true)
        mImageView.setImageBitmap(resizedBitmap)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Do nothing
    }

}
