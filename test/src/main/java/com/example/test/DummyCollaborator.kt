package com.example.test

import java.util.*

class DummyCollaborator {

    companion object {
        val ERROR_CODE: Int = 1;
    }

    fun doSomethingAsynchronously(callback: DummyCallback) {
        Thread(Runnable() {
            @Override
            fun run() {
                try {
                    Thread.sleep(5000);
                    callback.onSuccess(Collections.EMPTY_LIST as List<String?>?);
                } catch (e: InterruptedException) {
                    callback.onFail(ERROR_CODE);
                    e.printStackTrace();
                }
            }
        }).start(); }
}