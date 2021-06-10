package com.example.kotlintest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

public class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv_showmsg = findViewById<TextView>(R.id.tv_showmsg);
        val btn_test = findViewById<Button>(R.id.btn_test);

        btn_test.setOnClickListener(View.OnClickListener {

            startActivity(Intent(this, SecondActivity::class.java))

            tv_showmsg.append("测试语法！")
            Toast.makeText(
                this, "测试语法呢", Toast.LENGTH_SHORT
            ).show()
            testMethod(tv_showmsg, "\n测试 方法 的语法")
            testArgs(tv_showmsg, btn_test, "\n测试 多参数 的定义")


            var ageStr: String? = null
            try {
                val age1 = ageStr!!.toInt();
            } catch (e: Exception) {
                tv_showmsg.append("\n空指针异常测试一：使用 !! 如果有空指针直接抛出异常")
            }
            val age2 = ageStr?.toInt()
            tv_showmsg.append("\n空指针异常测试二：使用 ? 如果有空指针直接返回 null " + age2)

            val age3 = ageStr?.toInt() ?: 99
            tv_showmsg.append("\n空指针异常测试三：使用 ? 结合 ?: 如果有空指针返回自己指定的值 " + age3)

            tv_showmsg.append("\nNULL == \"" + "\" ? " + (null == ""))   //"" != NULL


        })


    }


    //匿名函数
    fun testNiMing() {
        val sumLambda: (Int, Int) -> Int = { x, y -> x + y }
        println(sumLambda(1, 2))
    }


    private fun testArgs(tvShowmsg: TextView?, btnTest: Button?, s: String) {

        if (tvShowmsg != null) {
            tvShowmsg.append(s + " ; " + tvShowmsg.toString() + " ; " + btnTest.toString())
        }
    }


    fun testMethod(tv_showmsg: TextView, s: String) {
        tv_showmsg.append(s)
    }

}