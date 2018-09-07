package com.wilson.funweibo.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Scroller
import com.wilson.funweibo.R
import kotlinx.android.synthetic.main.pull_refresh_header.view.*

/**
 *
 * @Author： chezhou
 * @Create： $date$
 * @Description：
 *
 */
class KtPullRefresh(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : ViewGroup(context, attrs, defStyleAttr){


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val scroller = Scroller(context)

    private val headerView by lazy {
        val view = LayoutInflater.from(context).inflate(R.layout.pull_refresh_header, null)
        view

    }

    private var loadingAnimator: ObjectAnimator? = null
    private var status:Status = Status.NORMAL
    private var bounceRatio = 0.05f
    private var bounce = 1f

    companion object {
        private const val EFFECTIVE_DISTANCE = 250
        private const val SCROLL_SPEED = 650
        private const val RESISTANCE = 1.65f


    }

    enum class Status{
        NORMAL, TRY_REFRESH, REFRESH
    }

    init {
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        addView(headerView, ViewGroup.LayoutParams.MATCH_PARENT, 1000)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for( i in 0 until childCount){
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for(i in 0 until childCount){
            val child = getChildAt(i)
            when (child){
                headerView -> {
                    child.layout(0, 0-child.measuredHeight,measuredWidth,0)
                }
                else ->{
                    child.layout(0,0,measuredWidth,measuredHeight)
                }

            }
        }
    }

    private var oldY = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {



        return super.onInterceptTouchEvent(ev)
    }


    override fun onTouchEvent(ev: MotionEvent?): Boolean {

        when(ev?.action) {

            MotionEvent.ACTION_DOWN -> {
                oldY = ev.y
            }

            MotionEvent.ACTION_MOVE -> {
                val off = oldY - ev.y
                val absY = Math.abs(scrollY)
                if(status != Status.REFRESH){
                    if(off < 0 ){
                        if(absY <= measuredHeight/2){
                            scrollBy(0, off.int(false))
                            if(absY >= EFFECTIVE_DISTANCE){
                                updateHeader(Status.TRY_REFRESH)
                            }
                        }

                    } else if(off > 0){
                        if(scrollY<0){
                            scrollBy(0,off.int(true))
                            if(absY< EFFECTIVE_DISTANCE){
                                updateHeader(Status.NORMAL)
                            }

                        }else{
                            bounce = 1f
                            updateHeader(Status.NORMAL)
                        }

                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                val absY = Math.abs(scrollY)
                if(absY < EFFECTIVE_DISTANCE){
                    bounce = 1f
                    updateHeader(Status.NORMAL)
                    scroller.startScroll(0, scrollY,0,-scrollY, SCROLL_SPEED)
                } else {
                    updateHeader(Status.REFRESH)
                    val resetY =  scrollY + 180
                    scroller.startScroll(0, scrollY,0, -resetY, SCROLL_SPEED)
                }

            }
        }
        oldY = ev!!.y
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.currY)
        }
        postInvalidate()
    }

    private fun dipToPx(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics)
    }



    private fun updateHeader(status: Status){
        this.status = status
        when (status){

            Status.NORMAL -> {
                if(headerView.tv_header_text.text!= "Pull Down") {
                    headerView.iv_header_loading.visibility = View.GONE
                    headerView.iv_header_arrow.visibility = View.VISIBLE
                    headerView.iv_header_arrow.setAnimate(false)
                    headerView.tv_header_text.text = "Pull Down"
                }
            }

            Status.TRY_REFRESH -> {
                if(headerView.tv_header_text.text!= "Release") {
                    headerView.iv_header_loading.visibility = View.GONE
                    headerView.iv_header_arrow.visibility = View.VISIBLE
                    headerView.iv_header_arrow.setAnimate(true)
                    headerView.tv_header_text.text = "Release"
                }

            }

            Status.REFRESH -> {
                headerView.iv_header_arrow.visibility = View.GONE
                headerView.iv_header_loading.visibility = View.VISIBLE
                headerView.tv_header_text.text = "Loading"
                headerView.iv_header_loading.loading()
            }



        }

    }

    private fun ImageView.setAnimate(start:Boolean = true, duration:Long = 500){
        this.animate().cancel()
        loadingAnimator?.cancel()
        this.animate().rotationX(if (start) 180f else 0f).setDuration(duration).start()
    }

    private fun ImageView.loading(){
        if(loadingAnimator == null){
            loadingAnimator = ObjectAnimator.ofFloat(this,"rotation", 0f, 359f)
        }
        loadingAnimator?.cancel()
        loadingAnimator?.repeatCount = ValueAnimator.INFINITE
        loadingAnimator?.interpolator = LinearInterpolator()
        loadingAnimator?.duration = 1000
        loadingAnimator?.start()
    }

    private fun Float.int(positive:Boolean = true): Int{
        if(positive){
            bounce+=bounceRatio
        }else
            bounce-=bounceRatio
        if(bounce>1){
            bounce = 1f
        }else if(bounce<0.25){
            bounce = 0.25f
        }
        return (this*bounce).toInt()
    }


}
