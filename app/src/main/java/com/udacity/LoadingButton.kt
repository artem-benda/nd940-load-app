package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private val textSize = 40.0f

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2.0f
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            color = getColor(R.styleable.LoadingButton_textColor, 0)
            strokeWidth = 2.0f
        }
        textAlign = Paint.Align.CENTER
        textSize = this@LoadingButton.textSize
        style = Paint.Style.STROKE
        setShadowLayer(5f, 0f, 0f, Color.GRAY)
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2.0f
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            color = getColor(R.styleable.LoadingButton_loadingIndicatorColor, 0)
            strokeWidth = 2.0f
        }
        style = Paint.Style.FILL
    }

    private var progressRect = Rect(null)

    private var fakeProgress = 0
        set(value) {
            field = value
            progressRect = Rect(0, 0, value, heightSize)
            postInvalidateOnAnimation()
        }

    private var buttonColor = 0
    private var textColor = 0
    private var loadingIndicatorColor = 0

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                buttonState = ButtonState.Loading
            }
            ButtonState.Loading -> {
                valueAnimator.apply {
                    setObjectValues(0, widthSize - 1)
                    duration = 3000L
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            buttonState = ButtonState.Completed
                        }
                    })
                    addUpdateListener {
                        fakeProgress = it.animatedValue as Int
                    }
                }.start()
            }
            ButtonState.Completed -> {
                fakeProgress = 0
                invalidate()
            }
        }
    }

    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            loadingIndicatorColor = getColor(R.styleable.LoadingButton_loadingIndicatorColor, 0)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        valueAnimator.cancel()
        widthSize = w
        heightSize = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(buttonColor)
        if (fakeProgress > 0) {
            canvas?.drawRect(progressRect, progressPaint)
        }
        when (buttonState) {
            ButtonState.Loading -> canvas?.drawText("Loading...", widthSize / 2.0f, (heightSize + textSize) / 2.0f, textPaint)
            else -> canvas?.drawText("Download", widthSize / 2.0f, (heightSize + textSize) / 2.0f, textPaint)
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

    override fun performClick(): Boolean {
        if (buttonState == ButtonState.Completed) {
            buttonState = ButtonState.Clicked
        }
        return super.performClick()
    }
}
