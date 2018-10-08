package com.tinyappco.shoplist

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Paint
import android.widget.TextView

fun TextView.toggleStrikeThrough(on: Boolean) {
    if (on){
        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

fun Activity.isLandscape() : Boolean {
    return this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}