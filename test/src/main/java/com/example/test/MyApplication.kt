package com.example.test

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object {
        private var mContext: Application? = null
        fun getContext(): Context {
            return mContext!!
        }

    }
}