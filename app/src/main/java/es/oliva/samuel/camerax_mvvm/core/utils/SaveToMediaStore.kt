package es.oliva.samuel.camerax_mvvm.core.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
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

    fun loadVideoThumbnailAsBitmap(context: Context, videoUri: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, videoUri)

            return retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            retriever.release()
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

    fun generateVideoFileName(imageName: String = "MyVideo"): String {
        val timeStamp: String = SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss-SSS", Locale.US
        ).format(System.currentTimeMillis())
        return "${imageName}_${timeStamp}.mp4"
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

    fun generatePublicVideoContentValuesForVideo(videoFileName: String): ContentValues {
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, videoFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
        }
    }

    fun generatePublicVideoUriForVideo(context: Context, videoFileName: String): Uri? {
        val contentValues = generatePublicVideoContentValuesForVideo(videoFileName)

        return context.contentResolver.insert(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues
        )
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        return getFilePathFromUri(context, uri)?.let { File(it) }
    }

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.MediaColumns.DATA)

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                filePath = cursor.getString(columnIndex)
            }
        }

        return filePath
    }
}
