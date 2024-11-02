package es.oliva.samuel.camerax_mvvm.core.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

object SaveToMediaStore {
    fun loadImageAsBitmap(context: Context, imageUri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT < 28) MediaStore.Images.Media.getBitmap(
                context.contentResolver, imageUri
            )
            else context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: FileNotFoundException) {
            Log.e("loadImageAsByteArray", "File Not Found Exception: ${e.message}")
            null
        } catch (e: IOException) {
            Log.e("loadImageAsByteArray", "IO Exception: ${e.message}")
            null
        }
    }

    /**
     * Save Bitmap Image to File
     */
    fun saveImageFromBitmap(
        context: Context,
        imageBitmap: Bitmap,
        saveUri: Uri,
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): Boolean {
        return try {
            context.contentResolver.openOutputStream(saveUri)?.use { outputStream ->
                imageBitmap.compress(compressFormat, 100, outputStream)
            }
            true
        } catch (e: FileNotFoundException) {
            Log.e("saveImageToInternalStorage", "File Not Found Exception: ${e.message}")
            false
        } catch (e: IOException) {
            Log.e("saveImageToInternalStorage", "IO Exception: ${e.message}")
            false
        }
    }

    /**
     * Delete file at Uri
     */
    fun deleteImage(context: Context, imageUri: Uri) {
        context.contentResolver.delete(imageUri, null, null)
    }

    /**
     * Get properties
     */
    fun generateImageFileName(imageName: String = "MyImage"): String {
        val timeStamp: String = SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss-SSS", Locale.US
        ).format(System.currentTimeMillis())
        return "${imageName}_${timeStamp}.jpeg"
    }

    fun generatePublicPictureUriForImage(context: Context, imageFileName: String): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        )
    }
}
