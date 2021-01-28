package com.byl.pin.util

import android.content.Context
import com.byl.pin.bean.RemainDateBean

object Utils {
    fun dp2Px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun getRemainSec(timestamp: Long): Long {
        return (timestamp - System.currentTimeMillis()) / 1000
    }

    fun getRemainDate(secs: Long): RemainDateBean? {
        if (secs <= 0) return null
        val remainDateBean = RemainDateBean()
        val day = secs / (60 * 60 * 24)
        val hour = secs / (60 * 60) - day * 24
        val min = secs / 60 - day * 24 * 60 - hour * 60
        val sec = secs - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60
        remainDateBean.day = "" + day
        if (hour >= 10) {
            remainDateBean.hour = "" + hour
        } else {
            remainDateBean.hour = "0$hour"
        }
        if (min >= 10) {
            remainDateBean.minute = "" + min
        } else {
            remainDateBean.minute = "0$min"
        }
        if (sec >= 10) {
            remainDateBean.second = "" + sec
        } else {
            remainDateBean.second = "0$sec"
        }
        return remainDateBean
    }
}