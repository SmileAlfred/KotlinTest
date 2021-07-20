package com.example.test

interface DummyCallback {
    fun onSuccess(result: List<String?>?)
    fun onFail(code: Int)
}
