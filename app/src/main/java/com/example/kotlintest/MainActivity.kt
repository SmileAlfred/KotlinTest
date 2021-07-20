package com.example.kotlintest

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintest.Utils.getAvailSpace
import com.example.kotlintest.Utils.getUnit
import com.example.kotlintest.Utils.queryStorage
import com.example.kotlintest.databinding.ActivityMainBinding
import com.example.kotlintest.mvvm.MvvmTestActivity
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding.root)

        //即使使用 binding 也一定要用本语句
        binding.btnTestyufa?.setOnClickListener(this)
        binding.btnViewbindnig?.setOnClickListener(this)
        binding.btnCoroutine1?.setOnClickListener(this)
        binding.btnCoroutline2?.setOnClickListener(this)
        binding.btnMrxjava?.setOnClickListener(this)
        binding.btnRoom?.setOnClickListener(this)
        binding.btnMvvm?.setOnClickListener(this)


        val padEmptySize = getAvailSpace(Environment.getDataDirectory().getAbsolutePath())
        Log.i(TAG, "设备剩余空间：" + getUnit(padEmptySize))
        queryStorage()
    }


    //匿名函数
    fun testNiMing() {
        val sumLambda: (Int, Int) -> Int = { x, y -> x + y }
        println(sumLambda(1, 2))
        println()
    }


    private fun testArgs(tvShowMsg: TextView?, btnTest: Button?, s: String) {

        if (tvShowMsg != null) {
            tvShowMsg.append(s + " ; " + tvShowMsg.toString() + " ; " + btnTest.toString())
        }
    }


    fun testMethod(tv_showmsg: TextView, s: String) {
        tv_showmsg.append(s)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnTestyufa -> testYuFa()

            binding.btnViewbindnig -> startActivity(Intent(this, ViewBindTestActivity::class.java))

            binding.btnCoroutine1 -> startActivity(Intent(this, CoroutineTestActivity1::class.java))

            binding.btnCoroutline2 -> startActivity(
                Intent(this, CoroutineTestActivity2::class.java)
            )

            //binding.btnMrxjava->startActivity(Intent(this, ::class.java))


            binding.btnMvvm -> startActivity(Intent(this, MvvmTestActivity::class.java))
        }
    }

    private fun testYuFa() {
        binding.tvShowmsg?.append("测试语法！")
        Toast.makeText(
            this, "测试语法呢", Toast.LENGTH_SHORT
        ).show()
        //testMethod(tv_showmsg, "\n测试 方法 的语法") //报错
        binding.tvShowmsg?.let { testMethod(it, "\n测试 方法 的语法") }

        testArgs(binding.tvShowmsg, binding.btnTestyufa, "\n测试 多参数 的定义")


        var ageStr: String? = null
        try {
            val age1 = ageStr!!.toInt();
        } catch (e: Exception) {
            binding.tvShowmsg?.append("\n空指针异常测试一：使用 !! 如果有空指针直接抛出异常")
        }
        val age2 = ageStr?.toInt()
        binding.tvShowmsg?.append("\n空指针异常测试二：使用 ? 如果有空指针直接返回 null " + age2)

        val age3 = ageStr?.toInt() ?: 99
        binding.tvShowmsg?.append("\n空指针异常测试三：使用 ? 结合 ?: 如果有空指针返回自己指定的值 " + age3)

        binding.tvShowmsg?.append("\nNULL == \"" + "\" ? " + (null == ""))   //"" != NULL

    }

}