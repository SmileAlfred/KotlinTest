package com.example.kotlintest

import java.util.*

/**
 *  4) Mock普通类的静态方法
 *  object 即 final 类
 */
class MUtils {

    //静态方法
    companion object {
        // 包裹范围内 属于静态方法
        fun generateNewUUId(): String {
            return UUID.randomUUID().toString()
        }
    }
}