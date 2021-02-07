package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var customBackgroundColor: Int = 0
    private var currentText = ""

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.white)
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

    }


    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            customBackgroundColor = getColor(R.styleable.LoadingButton_customBackgroundColor, 0)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawColor(customBackgroundColor)
            if (buttonState is ButtonState.Loading) {
                currentText = context.getString(R.string.button_loading_text)
            } else {
                currentText = context.getString(R.string.button_download_text)
            }

            drawText(currentText, widthSize / 2f, (heightSize / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f), textPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}