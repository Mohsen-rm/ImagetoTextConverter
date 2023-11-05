package com.ajiashi.imagetotextconverter

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var btn_get_image : ImageView
    lateinit var btn_clear : ImageView
    lateinit var btn_copy : ImageView
    lateinit var text_show : TextView
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    lateinit var imageUrl : Uri
    var vaule : String = ""
    lateinit var snackbar : Snackbar
    lateinit var relativeLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text_show = findViewById(R.id.text_show)
        btn_get_image = findViewById(R.id.get_img)
        btn_clear = findViewById(R.id.img_clear)
        btn_copy = findViewById(R.id.img_copy)
        relativeLayout = findViewById(R.id.page)

        btn_copy.setOnClickListener {
            copy_text()
        }

        btn_clear.setOnClickListener {
            text_show.setText("Here the image text appears")
            vaule = ""
        }

        btn_get_image.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            imageUrl = data?.data!!
            recognitionText()
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun recognitionText() {
        if (imageUrl != null){

            val image: InputImage

            try {
                image = InputImage.fromFilePath(this@MainActivity, imageUrl!!)

                val result = recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        // Task completed successfully
                        text_show.setText(visionText.text)
                        vaule = visionText.text
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        Toast.makeText(this@MainActivity,"Error in this probleam work",Toast.LENGTH_LONG).show()
                    }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun copy_text() {

        if(vaule.isEmpty()){
            val snack = Snackbar.make(
                findViewById(android.R.id.content),
                "There is no text to copy",
                Snackbar.LENGTH_LONG
            )
            snack.show()
        }else{
            // حصول على مشغل الحافظة
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // إعداد النص للنسخ
            val clipData = ClipData.newPlainText("Data", text_show.text.toString())
            // نسخ النص إلى الحافظة
            clipboardManager.setPrimaryClip(clipData)

            val snack = Snackbar.make(
                findViewById(android.R.id.content),
                "Successfully copied to clipboard",
                Snackbar.LENGTH_LONG
            )
            snack.show()
        }

    }

    fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, message, duration).show()
    }
}