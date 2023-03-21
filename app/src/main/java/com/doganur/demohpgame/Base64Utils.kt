package com.doganur.demohpgame

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.ImageButton
import android.widget.ImageView
import java.io.ByteArrayOutputStream

fun getUriFromName(fileName: String) = Uri.parse("android.resource://com.doganur.demohpgame/drawable/$fileName")

// Returns Base64 string
fun Activity.encodeFromUri(imageUri: Uri): String {
    val input = contentResolver.openInputStream(imageUri)
    val image = BitmapFactory.decodeStream(input, null, null)

    // Encode image to base64 string
    val baos = ByteArrayOutputStream()
    image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageBytes = baos.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}

fun ImageView.decodeFromBase64(imageString: String) {
    val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

    setImageBitmap(decodedImage)
}

fun ImageButton.decodeFromBase64(imageString: String) {
    val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

    setImageBitmap(decodedImage)
}