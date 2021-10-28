package com.iranwebyar.occasions.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.iranwebyar.occasions.R
import kotlin.math.roundToInt

class WheelView : View {
    private var mTextPaint: Paint? = null
    private var selectColor = 0
    private var normalColor = 0
    private var textSize = 0f
    private var selectIndex = 0
    private var lines = 0
    private var viewWidth = 0f
    private var viewHeight = 0f
    private var itemHeight = 0f
    private var lineSpacing = 0f
    private var valueAnimator: ValueAnimator? = null
    private var lastPointY = 0f
    private var mDistance = 0f
    private var startX = 0f
    private var header: Entry? = null
    private var move: Entry? = null
    private var mSize = 0
    private var listener: OnScrollListener? = null
    private var mSelectedData: String? = null

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {

        // initialization properties
        val context = this.context
        val array = context.obtainStyledAttributes(attrs, R.styleable.WheelView)
        selectColor = array.getColor(R.styleable.WheelView_wheel_select_color, 452195315)
        normalColor = array.getColor(R.styleable.WheelView_wheel_normal_color, 0xFF6565)
        lineSpacing = array.getInteger(R.styleable.WheelView_wheel_line_spacing, 10).toFloat()
        lines = array.getInt(R.styleable.WheelView_wheel_lines, 5)
        array.recycle()
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.style = Paint.Style.FILL
        textPaint.color = normalColor
        textPaint.strokeWidth = 5f
        textPaint.textAlign = Paint.Align.CENTER
        mTextPaint = textPaint
        header = Entry() // list head node
        move = header // mobile node
        move!!.next = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initWH()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (move!!.next == null) {
            return
        }
        canvas.save()
        val min = selectIndex - lines / 2 // a first element index is calculated 
        val max = selectIndex + lines / 2 // calculate the index of the last element
        for (i in min..max) {
            val startY = itemHeight * (max - i + 1) - lineSpacing + mDistance
            if (i == selectIndex) {
                mTextPaint!!.color = selectColor
                mSelectedData = getLinkedListData(i)
                // selected values ​​to the listener
                if (listener != null) {
                    listener!!.selected(mSelectedData)
                }
            } else {
                mTextPaint!!.color = normalColor
            }
            canvas.drawText(getLinkedListData(i) + "", startX, startY, mTextPaint!!)
        }
        canvas.restore()
    }

    /**
     * Set data
     * @param data
     */
    fun setData(data: List<String?>) {
        val size = data.size
        for (i in 0 until size) { // create a circular list
            mSize++
            val entry = Entry()
            entry.data = data[i]
            move!!.next = entry
            move = entry
            entry.next = null
        }
        move!!.next = header!!.next // connected end to end
        invalidate()
    }

    /**
     * Get data circular list
     *
     * @param i
     * @return
     */
    private fun getLinkedListData(i: Int): String? {
        var entry = move!!.next // first element
        var size = i % mSize
        if (size < 0) { // If size = -1, in fact, is the last element of the circular linked list, and so on
            size += mSize
        }
        for (j in 0 until size) {
            entry = move!!.next
            move = entry
        }
        move = header // reset the mobile node
        return entry!!.data
    }

    /**
     * Initialize the width and height
     */
    private fun initWH() {
        viewWidth = measuredWidth.toFloat()
        viewHeight = measuredHeight.toFloat()
        itemHeight = viewHeight / lines
        textSize =
            itemHeight - 2 * lineSpacing // Save letter size according to the item high line spacing is calculated
        startX = viewWidth / 2
        mTextPaint!!.textSize = textSize
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastPointY = event.y // record the current location
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                mDistance += event.y - lastPointY // accumulated moving distance
                lastPointY = event.y
                updateSelectIndex()
                return true
            }
            MotionEvent.ACTION_UP -> {
                // roll back to the neutral position
                rollbackToCenter()
                return true
            }
        }
        return false
    }

    /**
     * Update the selected index value
     */
    private fun updateSelectIndex() {
        // more than one entry in half, chose to believe it
        val changedItemNumber = (mDistance / itemHeight).roundToInt()
        // calculate the index
        val lastSelectIndex = selectIndex
        selectIndex = lastSelectIndex + changedItemNumber
        // distance plus the change in the high entry
        mDistance += itemHeight * (lastSelectIndex - selectIndex)
        invalidate()
    }

    /**
     * Data elements (static inner classes to solve memory leaks)
     */
    private class Entry {
        var data: String? = null
        var next: Entry? = null
    }

    /**
     * Roll back to the neutral position
     */
    private fun rollbackToCenter() {
        if (valueAnimator == null) {
            val animator = ValueAnimator.ofFloat(mDistance, 0f)
            animator.duration = 240
            animator.addUpdateListener {
                val `val` = animator.animatedValue
                if (`val` is Float) {
                    mDistance = `val`
                    invalidate()
                }
            }
            animator.interpolator = LinearInterpolator()
            valueAnimator = animator
        } else {
            valueAnimator!!.cancel()
            valueAnimator!!.setFloatValues(mDistance, 0f)
        }
        valueAnimator!!.start()
    }

    /**
     * Set slide monitor
     * @param onScrollListener
     */
    fun setOnSelectedListener(onScrollListener: OnScrollListener?) {
        listener = onScrollListener
    }

    /**
     * Slide listening Interface
     */
    interface OnScrollListener {
        fun selected(data: String?)
    }

    /**
     * Set the selected index
     * @param selectIndex
     */
    fun setSelectIndex(selectIndex: Int) {
        this.selectIndex = selectIndex + 1 // add a correction index
    }
}