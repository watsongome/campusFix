package com.example.campusfix.ui.report

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import java.io.File

/** Creates an empty .jpg in the app's cache and returns a content:// Uri the camera app can write into. */
fun createImageUri(context: Context): Uri {
    val dir = File(context.cacheDir, "report_photos").apply { mkdirs() }
    val file = File.createTempFile("report_", ".jpg", dir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

/** Small preview (1/8 size) so a full camera photo is never loaded into memory just to show a thumbnail. */
fun decodeThumbnail(context: Context, uri: Uri): ImageBitmap? = runCatching {
    val options = BitmapFactory.Options().apply { inSampleSize = 8 }
    context.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, options)
    }?.asImageBitmap()
}.getOrNull()
