package com.byl.pin.bean

class RemainDateBean {
    var day = "0"
    var hour = "00"
    var minute = "00"
    var second = "00"
    var totalSec = 0 //总秒数

    constructor() {}

    constructor(day: String, hour: String, minute: String, second: String) {
        this.day = day
        this.hour = hour
        this.minute = minute
        this.second = second
    }
}