package de.syntax.androidabschluss.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CustomDrawView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    var bitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private var canvasBitmap: Canvas = Canvas(bitmap)
    private val paintClear = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        strokeWidth = 20f
        color = Color.TRANSPARENT
        style = Paint.Style.FILL_AND_STROKE
    }

    init {
        bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT)
        canvasBitmap = Canvas(bitmap)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val oldBitmap = bitmap
        bitmap = Bitmap.createBitmap(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec), Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT)
        canvasBitmap = Canvas(bitmap)
        canvasBitmap.drawBitmap(oldBitmap, 0f, 0f, null)
        oldBitmap.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                canvasBitmap.drawCircle(x, y, paintClear.strokeWidth, paintClear)
                invalidate()
            }
        }
        return true
    }
    fun updateStrokeWidth(strokeWidth: Float) {
        paintClear.strokeWidth = strokeWidth
        invalidate()
    }

    fun setInitialBitmap(newBitmap: Bitmap) {
        bitmap = Bitmap.createBitmap(newBitmap.width, newBitmap.height, Bitmap.Config.ARGB_8888)
        val newCanvas = Canvas(bitmap)
        newCanvas.drawBitmap(newBitmap, 0f, 0f, null)
        canvasBitmap = newCanvas
        invalidate()
    }
}
