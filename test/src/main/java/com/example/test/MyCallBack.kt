package com.example.test

interface MyCallBack {
    fun stateEntered(state: Int):Int

    fun stateExited(state: Int):Boolean

    fun stateChanged(from: Int, to: Int):String
}