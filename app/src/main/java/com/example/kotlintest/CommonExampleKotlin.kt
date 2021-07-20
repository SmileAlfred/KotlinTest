package com.example.kotlintest

import java.io.File

class CommonExampleKotlin {

    /**
     * 7) Mock普通类的私有变量
     */
    private val STATE_NOT_READY: Int = 0
    private val STATE_READY = 1

    private val mState = STATE_NOT_READY

    open fun doSomethingIfStateReady(): Boolean {
        return if (mState == STATE_READY) {
            // DO some thing
            true
        } else {
            false
        }
    }

    /**
     * 6) Mock系统类的静态和final方法
     */
    fun callSystemStaticMethod(str: String?): String {
        return System.getProperty(str)
    }

    /**
     * 1) 普通Mock：Mock参数传递的对象
     */
    fun callArgumentInstance(file: File): Boolean {
        return file.exists()
    }

    /**
     * 2) Mock方法内部new出来的对象
     */
    fun callArgumentInstance(path: String?): Boolean {
        val file = File(path)
        return file.exists()
    }

    /**
     * 4) Mock普通类的静态方法
     */
    fun printUUID(): String {
        return MUtils.generateNewUUId()
    }

    /**
     * 5) Mock 私有方法
     */
    fun callPrivateMethod(): Boolean {
        return isExist()
    }

    private fun isExist(): Boolean {
        return false
    }


}