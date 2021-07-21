package com.example.test.asyn

/**
 * Client是一个通信客户端，
 * 在其内部维护了一个Connector类的实例来完成Socket层面的send/recv等消息的发送和接收的工作。
 * 有一个IListener接口来处理收到的消息，当Connector收到消息时，
 * 将调用IListener的onMessage方法来处理消息。这样就完成了消息的异步处理
 */
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