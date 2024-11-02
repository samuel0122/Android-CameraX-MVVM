package es.ua.eps.camerax_mvvm.ui.common.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import es.ua.eps.camerax_mvvm.R

class VerticalSeekBar : AppCompatSeekBar {

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        thumb = ContextCompat.getDrawable(context, R.drawable.ic_progress_bar_thumb)
    }

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        updateThumb(progress)
    }

    private fun updateThumb(progress: Int) {
        val thumbDrawable = thumb as? LayerDrawable ?: return
        val fillDrawable = thumbDrawable.findDrawableByLayerId(R.id.fill)
        val level = (progress / max.toFloat() * 10000).toInt()
        fillDrawable?.level = level
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.rotate(-90f)
        canvas.translate(-height.toFloat(), 0f)
        super.onDraw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                val progress = max - (max * event.y / height).toInt()
                setProgress(progress)
                onSizeChanged(width, height, 0, 0)
            }

            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }
}
