package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var customBackgroundColor: Int = 0

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.white)
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    private val backgroundPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorPrimary)
    }

    private val backgroundProgressPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorPrimaryDark)
    }

    private val circlePaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
    }

    private var valueAnimator = ValueAnimator()
    private var circleAnimator = ValueAnimator()
    private var currentProgress: Float = 0f
    private var currentCircleAngle: Float = 0f

    var buttonState: ButtonState? = null
        set(value) {
            if (field == value) {
                return
            }

            when (value) {
                is ButtonState.Loading -> {
                    showLoading()
                }
                ButtonState.Completed -> {
                    finishAnimation()
                }
                else -> {
                    return
                }
            }
            field = value
            invalidate()
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            customBackgroundColor = getColor(R.styleable.LoadingButton_customBackgroundColor, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            if (buttonState is ButtonState.Loading) {
                drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), backgroundPaint)
                drawRect(0f, 0f, currentProgress, heightSize.toFloat(), backgroundProgressPaint)
                drawText(
                    context.getString(R.string.button_loading_text),
                    widthSize / 2f,
                    (heightSize / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f),
                    textPaint
                )


                val size = measuredHeight.toFloat() - paddingBottom.toFloat()
                drawArc(paddingStart.toFloat(),
                    paddingTop.toFloat(), size, size, 0f, currentCircleAngle, true, circlePaint)

            } else {
                drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), backgroundPaint)
                drawText(
                    context.getString(R.string.button_download_text),
                    widthSize / 2f,
                    (heightSize / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f),
                    textPaint
                )
            }
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

    private fun showLoading() {
        valueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat()).apply {
            duration = 2000
            addUpdateListener { animation ->
                currentProgress = animation.animatedValue as Float
                invalidate()
            }
            doOnEnd { buttonState = ButtonState.Completed }
            start()
        }

        circleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 2000
            addUpdateListener { animation ->
                currentCircleAngle = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun finishAnimation() {
        valueAnimator.removeAllUpdateListeners()
        circleAnimator.removeAllUpdateListeners()
    }

}