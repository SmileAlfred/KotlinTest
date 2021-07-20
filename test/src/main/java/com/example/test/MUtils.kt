package com.example.test

import android.content.Context
import java.util.*

/**
 *  4) Mock 静态 类的静态方法
 *  object 即 final 类
 */
object MUtils {

    /*//静态方法
    companion object {
        // 包裹范围内 属于静态方法
    } */

    @JvmStatic
    open fun generateNewUUId(): String {
        return UUID.randomUUID().toString()
    }

    @JvmStatic
    fun getContext():Context{
        return MyApplication.getContext()
    }

}
