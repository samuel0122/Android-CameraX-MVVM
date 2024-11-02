package es.ua.eps.camerax_mvvm.ui.common.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View

class FocusCircleView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private var focusCircle: RectF? = null

    private var handler = Handler(Looper.getMainLooper())
    private var removeFocusRunnable = Runnable { }

    fun setRectPoint(x: Float, y: Float) {
        focusCircle = RectF(x - 100, y - 100, x + 100, y + 100)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        focusCircle?.let {
            val width = it.width()
            val lineLength = width * 0.2f

            canvas.drawLine(it.left, it.top, it.left + lineLength, it.top, paint)
            canvas.drawLine(it.left, it.top, it.left, it.top + lineLength, paint)

            canvas.drawLine(it.right, it.top, it.right - lineLength, it.top, paint)
            canvas.drawLine(it.right, it.top, it.right, it.top + lineLength, paint)

            canvas.drawLine(it.left, it.bottom, it.left + lineLength, it.bottom, paint)
            canvas.drawLine(it.left, it.bottom, it.left, it.bottom - lineLength, paint)

            canvas.drawLine(it.right, it.bottom, it.right - lineLength, it.bottom, paint)
            canvas.drawLine(it.right, it.bottom, it.right, it.bottom - lineLength, paint)


            val centerX = it.centerX()
            val centerY = it.centerY()
            val radiusOuter = width * 0.35f
            canvas.drawCircle(centerX, centerY, radiusOuter, paint)

            val radiusInner = width * 0.25f
            canvas.drawCircle(centerX, centerY, radiusInner, paint)

            scheduleFocusCircleRemoval()
        }
    }

    private fun scheduleFocusCircleRemoval() {
        handler.removeCallbacks(removeFocusRunnable)
        removeFocusRunnable = Runnable {
            focusCircle = null
            invalidate()
        }
        handler.postDelayed(removeFocusRunnable, 2000)
    }
}