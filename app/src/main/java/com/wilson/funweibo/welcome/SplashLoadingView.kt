package com.wilson.funweibo.welcome

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

/**
 *
 * @Author： chezhou
 * @Create： $date$
 * @Description：
 *
 */
class SplashLoadingView: View {

    companion object {
        private const val SKIP_TEXT = "Skip"
        private const val ROUND_STROKE_WIDTH = 10f
        private const val ANIMATION_DURATION = 5000L
        private const val ANIMATION_INTERVAL = 50L
    }


    private var textPaint: Paint = TextPaint().apply {
        color = Color.BLACK
        isAntiAlias = true
        textSize = 28f
        textAlign = Paint.Align.CENTER
    }

    private var roundPaint: Paint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.LTGRAY
        strokeWidth = ROUND_STROKE_WIDTH
        isAntiAlias = true
    }

    private var roundRect: RectF = RectF()
    private var strokeRect: RectF = RectF()
    private var baseLine = 0f
    private var progress = 0f
    lateinit var onFinish: () -> Unit
    private var isFinish = false


    private var radius =  0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        radius = if (width > height) height/2.toFloat() else width/2.toFloat()
        radius -= ROUND_STROKE_WIDTH / 2
        roundRect.set(0f,0f,width.toFloat(),height.toFloat())
        val distance = (textPaint.fontMetrics.bottom - textPaint.fontMetrics.top)/2 - textPaint.fontMetrics.bottom
        baseLine = roundRect.centerY() + distance
        var strokeWidth = ROUND_STROKE_WIDTH *4/ 5
        strokeRect.set(strokeWidth, strokeWidth,width - strokeWidth, height - strokeWidth)

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        roundPaint.color = Color.LTGRAY
        roundPaint.style = Paint.Style.FILL_AND_STROKE
        canvas?.drawCircle(width/2.toFloat(), height/2.toFloat(),radius,roundPaint)
        canvas?.drawText(SKIP_TEXT,roundRect.centerX(),baseLine, textPaint)

        roundPaint.color = Color.WHITE
        roundPaint.style = Paint.Style.STROKE
        canvas?.drawArc(strokeRect,-90f, (progress * 360)/100, false, roundPaint)

    }

    fun setListener(listener: () -> Unit){
        this.onFinish = listener
    }

    fun startProgress() {
        if(isFinish){
            return
        }
       postDelayed({
           progress += (ANIMATION_INTERVAL *100/ ANIMATION_DURATION)
           invalidate()
           if(progress<100){
               startProgress()
           }else if(!isFinish){
               onFinish.invoke()

           }

       }, ANIMATION_INTERVAL)
    }

    fun stopProgress(){
        this.isFinish = true
    }



}
