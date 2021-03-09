package com.example.androiddevchallenge.extensions

import android.text.format.DateUtils

fun Long.formatElapsedTime(): String = DateUtils.formatElapsedTime(this / DateUtils.SECOND_IN_MILLIS)