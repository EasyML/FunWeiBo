package com.wilson.funweibo.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Scroller
import com.wilson.funweibo.R
import kotlinx.android.synthetic.main.refresh_header.view.*

class KtPullRefresh: ViewGroup {

    constructor(context: Context?): super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)



    companion object {
        private const val EFFECTIVE_SCROLL = 300
        private const val SCROLL_SPEED = 650
        private const val STOP_SPEED = 100
        private const val RESISTANCE = 0.01f
    }

    enum class Status{
        NORMAL,TRY_REFRESH, REFRESH
    }

    private var pullRefreshListener: (() -> Unit)? = null
    private val mScroller = Scroller(context)
    private var loadingAnimator: ObjectAnimator? = null
    private var mStatus:Status = Status.NORMAL
    private var initialResist = 1.0f

    private val headerView by lazy {

        LayoutInflater.from(context).inflate(R.layout.refresh_header, null)

    }
    override fun onFinishInflate() {
        super.onFinishInflate()
        addView(headerView, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1000))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        for (i in 0 until childCount){
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

        for ( i in 0 until childCount){
            val child = getChildAt(i)

            when(child) {

                headerView -> {
                    child.layout(0, -child.measuredHeight, child.measuredWidth, 0)
                }
                else -> {
                    child.layout(0,0,child.measuredWidth,child.measuredHeight)
                }


            }

        }
    }

    private var oldInterceptY = 0f
    private var shouldIntercept = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {


        when(ev?.action) {

            MotionEvent.ACTION_DOWN -> {

                oldInterceptY = ev.y
                shouldIntercept = false

            }

            MotionEvent.ACTION_MOVE -> {
                val offset = ev.y - oldInterceptY
                if(offset > 0){
                    val child  = getChildAt(0)
                    if( child is RecyclerView) {
                        if (child.computeVerticalScrollOffset()<=0){
                            shouldIntercept = true
                        }
                    }

                }else {
                    shouldIntercept = false
                }

            }

            MotionEvent.ACTION_UP -> {
                shouldIntercept = false
            }


        }
        oldInterceptY = ev!!.y

        return shouldIntercept
    }

    private var oldY = 0f
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val absY = Math.abs(scrollY)
        when(ev?.action) {

            MotionEvent.ACTION_DOWN -> {
                oldY = ev.y
            }

            MotionEvent.ACTION_MOVE -> {


                val offset = oldY - ev.y
                if(offset < 0){
                    if(absY <= headerView.measuredHeight-100 && mStatus != Status.REFRESH){
                        scrollBy(0, offset.int(true))
                        if(absY <= EFFECTIVE_SCROLL){
                            updateHeaderView(Status.NORMAL)
                        }else {
                            updateHeaderView(Status.TRY_REFRESH)
                        }
                    }

                } else {
                    if(scrollY<0 && mStatus != Status.REFRESH){
                        scrollBy(0, offset.int(false))
                        if(absY <= EFFECTIVE_SCROLL){
                            updateHeaderView(Status.NORMAL)
                        }else {
                            updateHeaderView(Status.TRY_REFRESH)
                        }
                    }

                }

            }

           MotionEvent.ACTION_UP -> {
               if(absY <= EFFECTIVE_SCROLL && mStatus != Status.REFRESH){
                   initialResist = 1f
                   updateHeaderView(Status.NORMAL)
                   mScroller.startScroll(0, scrollY,0, -scrollY, SCROLL_SPEED)
               }else {
                   updateHeaderView(Status.REFRESH)
                   mScroller.startScroll(0, scrollY,0, -scrollY-200, SCROLL_SPEED)
               }

           }

        }

        oldY = ev!!.y

        return true
    }


    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.currY)
        }
        postInvalidate()
    }


    private fun updateHeaderView(status: Status){
        this.mStatus = status
        when (status){

            Status.NORMAL -> {
                if(headerView.tv_header_text.text != "Pull Down"){
                    headerView.tv_header_text.text = "Pull Down"
                    headerView.iv_header_loading.visibility = View.GONE
                    headerView.iv_header_arrow.rotate(false)
                }

            }

            Status.TRY_REFRESH -> {
                if(headerView.tv_header_text.text != "Release"){
                    headerView.tv_header_text.text = "Release"
                    headerView.iv_header_loading.visibility = View.GONE
                    headerView.iv_header_arrow.rotate(true)
                }
            }

            Status.REFRESH -> {
                headerView.tv_header_text.text = "Loading"
                headerView.iv_header_arrow.visibility = View.GONE
                headerView.iv_header_loading.loading()
                pullRefreshListener?.invoke()
            }

        }
    }


    fun setRefreshListener(refresh: ()->Unit){
        this.pullRefreshListener = refresh
    }

    fun stopRefresh(){
         mScroller.startScroll(0, scrollY,0,-scrollY, STOP_SPEED)
         updateHeaderView(Status.NORMAL)
     }


    private fun ImageView.rotate(down: Boolean = true,duration: Long = 250) {

        this.visibility = View.VISIBLE
        loadingAnimator?.cancel()
        this.animate().cancel()
        this.animate().rotationX(if(down) 180f else 0f).setDuration(duration).start()
    }

    private fun ImageView.loading(duration: Long = 750) {
        this.visibility = View.VISIBLE
        if(loadingAnimator == null){
            loadingAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)
            loadingAnimator?.duration = duration
            loadingAnimator?.interpolator = LinearInterpolator()
            loadingAnimator?.repeatCount = ValueAnimator.INFINITE
        }
        loadingAnimator?.cancel()
        loadingAnimator?.start()


    }

    private fun Float.int(bias:Boolean = true): Int {

        if(bias){
            initialResist -= RESISTANCE
            if(initialResist<0.5f){
                initialResist = 0.5f
            }
        } else {
            initialResist += RESISTANCE
            if(initialResist>1.0f){
                initialResist = 1.0f
            }
        }
        return (this*initialResist).toInt()
    }
}