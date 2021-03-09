package com.example.androiddevchallenge.viewmodel

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    val millisInFuture = DateUtils.MINUTE_IN_MILLIS / 2
    val countdownInterval = DateUtils.SECOND_IN_MILLIS


    private val _millisUntilFinished = MutableLiveData(millisInFuture)
    val millisUntilFinished: LiveData<Long> = _millisUntilFinished

    fun onTimeChanged(newTime: Long) {
        _millisUntilFinished.value = newTime
    }
}