package com.example.test.asyn

class Listener(client: Client) : IListener {
    private var client: Client;

    init {
        this.client = client;
    }

    override fun onMessage(data: String) {
        System.out.println(data);
        if (data.contains("SUCCESS")) {
            client.setConnected(true);
        }
    }
}