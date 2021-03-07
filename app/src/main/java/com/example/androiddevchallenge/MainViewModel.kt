package com.example.androiddevchallenge

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    /**
     * Denotes whether timer is running, also the source of navigation
     */
    private val _hasTimerStarted = MutableLiveData(false)
    val hasTimerStarted: LiveData<Boolean>
        get() = _hasTimerStarted

    /**
     * Holds values for the currently running timer
     */
    val minutesRemaining: LiveData<Int>
        get() = _minutesRemaining
    private val _minutesRemaining = MutableLiveData<Int>()

    val secondsRemaining: LiveData<Int>
        get() = _secondsRemaining
    private val _secondsRemaining = MutableLiveData<Int>()

    /**
     * Holds the input values for which the timer will be started
     */
    private val _timerMinutes = MutableLiveData("00")
    val timerMinutes: LiveData<String>
        get() = _timerMinutes

    private val _timerSeconds = MutableLiveData("00")
    val timerSeconds: LiveData<String>
        get() = _timerSeconds

    private var minutesCount = 0
    private var secondsCount = 0

    private lateinit var timer: CountDownTimer

    /**
     * methods for setting the value for required duration of timer
     */
    fun incrementMinutes() {
        if(minutesCount<60) { //to prevent illegal minute values
            minutesCount++
            _timerMinutes.value = minutesCount.toProperFormat()
        }
    }

    fun incrementSeconds() {
        if(secondsCount<60) { //to prevent illegal second values
            secondsCount++
            _timerSeconds.value = secondsCount.toProperFormat()
        }
    }

    fun decrementMinutes() {
        if(minutesCount>0) {
            minutesCount--
            _timerMinutes.value = minutesCount.toProperFormat()
        }
    }

    fun decrementSeconds() {
        if(secondsCount>0) {
            secondsCount--
            _timerSeconds.value = secondsCount.toProperFormat()
        }
    }

    fun startTimer() {
        val totalSecs = (minutesCount * 60 * 1000) + secondsCount * 1000
        timer = object: CountDownTimer(totalSecs.toLong(), 1000){

            override fun onTick(millisUntilFinished: Long) {
                _minutesRemaining.value = (millisUntilFinished/(60 * 1000)).toInt()
                _secondsRemaining.value = ((millisUntilFinished%(60 * 1000))/1000).toInt()
                Log.i("Tick:"," ${_minutesRemaining.value} : ${_secondsRemaining.value}")
            }

            override fun onFinish() {
                _minutesRemaining.value = 0
                _secondsRemaining.value = 0
                _hasTimerStarted.value = false
            }
        }
        _hasTimerStarted.value = true
        timer.start()
    }

    fun stopTimer() {
        if(this::timer.isInitialized) timer.cancel()
        _minutesRemaining.value = 0
        _secondsRemaining.value = 0
        _hasTimerStarted.value = false
    }

    fun onBackPressed(): Boolean {
        val timerRunning: Boolean = if(_hasTimerStarted.value != null) _hasTimerStarted.value!! else false
        return if(timerRunning) {
            stopTimer()
            false
        } else {
            true
        }
    }

    override fun onCleared() {
        stopTimer()
    }
}

/**
 * Ext function to return values padded with 0 if single digit number. [Can be improvised]
 */
fun Int.toProperFormat(): String {
    return if(this < 10) "0$this" else this.toString()
}
