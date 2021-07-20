package com.example.test.asyn


class Client(connector: Connector, s: String) {
    private  var connector: Connector
    private  var server: String
    private var connected = false
    init {
        this.connector = connector
        this.server = s
        this.connector.setListener(Listener(this))
    }

    fun setConnected(connected: Boolean) {
        this.connected = connected
    }

    fun getConnected(): Boolean {
        return connected
    }

    fun login() {
        var times = 0
        // sending login request to remote
        connector.login(server)
        // waiting for response from network
        while (!getConnected() && times < 10) {
            try {
                Thread.sleep(100)
                times++
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}