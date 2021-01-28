package com.byl.pin.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.byl.pin.R
import com.byl.pin.databinding.ViewCountTimerBinding
import com.byl.pin.util.Utils.getRemainDate
import com.byl.pin.util.Utils.getRemainSec

class CountTimerView : FrameLayout {
    var v: ViewCountTimerBinding =
        ViewCountTimerBinding.inflate(LayoutInflater.from(context), this, true)
    var remainSec: Long = 0//剩余时间
    var myTask: MyTask? = null
    var running = false

    constructor(context: Context) : super(context) {
        initAttributeSet(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttributeSet(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttributeSet(attrs)
    }

    private fun initAttributeSet(attrs: AttributeSet?) {
        var color: String? = null
        var isShowTips = true
        var tips: String? = null
        var textSize = 0
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownView)
            color = typedArray.getString(R.styleable.CountDownView_textColorNormal)
            isShowTips = typedArray.getBoolean(R.styleable.CountDownView_showTips, true)
            tips = typedArray.getString(R.styleable.CountDownView_tips)
            textSize = typedArray.getInt(R.styleable.CountDownView_textSize, 0)
            typedArray.recycle()
        }
        if (!TextUtils.isEmpty(color)) {
            v.tvTips.setTextColor(Color.parseColor(color))
            v.tvTime.setTextColor(Color.parseColor(color))
        }
        if (textSize > 0) {
            v.tvTips.textSize = textSize.toFloat()
            v.tvTime.textSize = textSize.toFloat()
        }
        if (isShowTips) {
            v.tvTips.visibility = VISIBLE
            if (!TextUtils.isEmpty(tips)) {
                v.tvTips.text = tips
            }
        } else {
            v.tvTips.visibility = GONE
        }
        init()
    }

    private fun init() {
        myTask = MyTask()
    }

    @SuppressLint("SetTextI18n")
    fun setContent(remainSec: Long) {
        val remainDate = getRemainDate(remainSec) ?: return
        if (remainDate.day.toInt() > 0) {
            v.tvTime.text =
                remainDate.day + "天" + remainDate.hour + ":" + remainDate.minute + ":" + remainDate.second
        } else {
            v.tvTime.text = remainDate.hour + ":" + remainDate.minute + ":" + remainDate.second
        }
    }

    /**
     * 设置结束时间(时间戳)
     *
     * @param remainTime
     */
    fun setRemainTime(remainTime: Long) {
        remainSec = getRemainSec(remainTime)
        setContent(remainSec)
    }

    inner class MyTask : Runnable {
        override fun run() {
            if (remainSec == 0L) {
                stop()
                timerEnd
                return
            }
            remainSec--
            setContent(remainSec)
            postDelayed(myTask, 1000)
        }
    }

    fun start() {
        if (running) stop()
        running = true
        postDelayed(myTask, 0)
    }

    fun stop() {
        running = false
        removeCallbacks(myTask)
    }

    private lateinit var timerEnd: () -> Unit

    fun addTimerEndListener(timerEnd: () -> Unit) {
        this.timerEnd = timerEnd
    }

}