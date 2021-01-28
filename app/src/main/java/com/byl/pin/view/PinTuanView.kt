package com.byl.pin.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.children
import com.byl.pin.R
import com.byl.pin.bean.ItemBean
import com.byl.pin.databinding.ViewItemBinding
import com.byl.pin.databinding.ViewPinBinding
import com.byl.pin.util.Utils.dp2Px


class PinTuanView : FrameLayout {

    companion object {
        const val SHOW_COUNT = 2
        const val TIME_INTERVAL: Long = 3000
    }

    val v = ViewPinBinding.inflate(LayoutInflater.from(context), this, true)
    var listData: ArrayList<ItemBean>? = null
    var scrollTask: ScrollTask? = null
    var preShowIndex = 0

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

    }

    fun setData() {
        listData = ArrayList()
        for (i in 0..9) {
            val itemBean = ItemBean()
            itemBean.id = i
            itemBean.name = "哈哈哈" + (i + 1)
            itemBean.endTime = System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000
            listData!!.add(itemBean)
        }

        //如果list条数为不超过2个，则不执行滚动动画
        if (listData!!.size <= SHOW_COUNT) {
            listData!!.forEach {
                v.rootView.addView(getItemView(it))
            }
            return
        }

        //超过两个时，先向容器中添加三个Item
        for (i in 0..SHOW_COUNT) {
            v.rootView.addView(getItemView(listData!![i]))
        }

        preShowIndex = SHOW_COUNT + 1//即将显示的数据的index
    }

    fun getItemView(itemBean: ItemBean): View {
        val itemView = ViewItemBinding.inflate(LayoutInflater.from(context), null, false)
        itemView.tvName.text = itemBean.name
        itemView.mCountDownView.setRemainTime(itemBean.endTime)
        itemView.mCountDownView.start()
        itemView.mCountDownView.addTimerEndListener {
            //TODO 倒计时结束
        }
        itemView.btnAddTeam.setOnClickListener {
            clickTeam(itemBean)
        }
        return itemView.root
    }

    fun start() {
        if (scrollTask != null) {
            removeCallbacks(scrollTask)
            scrollTask = null
        }
        scrollTask = ScrollTask()
        postDelayed(scrollTask, TIME_INTERVAL)
    }

    fun stop() {
        removeCallbacks(scrollTask)
        scrollTask = null
        v.rootView.children.forEach {
            it.findViewById<CountTimerView>(R.id.mCountDownView).stop()
        }
    }

    inner class ScrollTask : Runnable {

        override fun run() {
            val height = dp2Px(context, 80f)
            val animator = ValueAnimator.ofInt(height)
            animator.duration = 500
            animator.start()
            animator.addUpdateListener { animation: ValueAnimator ->
                val value = animation.animatedValue as Int
                var layoutParams: LinearLayout.LayoutParams =
                    v.rootView.getChildAt(0).layoutParams as LinearLayout.LayoutParams
                layoutParams.topMargin = -value
                v.rootView.getChildAt(0).layoutParams = layoutParams
                v.rootView.getChildAt(0).alpha = (0.6f - value / height.toFloat())
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    v.rootView.getChildAt(0).findViewById<CountTimerView>(
                        R.id.mCountDownView
                    ).stop()
                    v.rootView.removeViewAt(0)
                    if (listData!!.size <= preShowIndex) {
                        preShowIndex = 0
                    }
                    v.rootView.addView(getItemView(listData!![preShowIndex]))
                    preShowIndex++
                    postDelayed(scrollTask, TIME_INTERVAL)
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
        }

    }


    private lateinit var clickTeam: (itemBean: ItemBean) -> Unit

    fun addClickTeamListener(clickTeam: (itemBean: ItemBean) -> Unit) {
        this.clickTeam = clickTeam
    }

}