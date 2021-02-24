package com.glennrosspascual.githubusers

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest


class ReverseBitmapTransformation : BitmapTransformation() {
    private val ID = "com.glennrosspascual.githubusers.ReverseBitmapTransformation"
    private val ID_BYTES: ByteArray = ID.toByteArray(Charset.forName("UTF-8"))

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {

        val colorMatrix_Inverted = ColorMatrix(
            floatArrayOf(
                -1f,
                0f,
                0f,
                0f,
                255f,
                0f,
                -1f,
                0f,
                0f,
                255f,
                0f,
                0f,
                -1f,
                0f,
                255f,
                0f,
                0f,
                0f,
                1f,
                0f
            )
        )

        val colorFilterSepia: ColorFilter = ColorMatrixColorFilter(
            colorMatrix_Inverted
        )

        val bitmap = Bitmap.createBitmap(
            toTransform.width, toTransform.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        val paint = Paint()

        paint.colorFilter = colorFilterSepia
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        return bitmap

    }



    override fun equals(o: Any?): Boolean {
        return o is ReverseBitmapTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }
}