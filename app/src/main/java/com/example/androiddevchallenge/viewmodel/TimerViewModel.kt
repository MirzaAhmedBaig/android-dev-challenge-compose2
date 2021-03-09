/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
