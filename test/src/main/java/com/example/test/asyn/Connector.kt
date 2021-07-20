package com.example.test.asyn

class Connector {
    private lateinit var listener: IListener
    fun onMessage(data: String) {
        listener.onMessage(data)
    }

    fun setListener(listener: IListener) {
        this.listener = listener
    }

    fun login(url: String) {
        println("sending login request to $url")
    }
}