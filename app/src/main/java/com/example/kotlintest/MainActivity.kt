package com.example.kotlintest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.kotlintest.databinding.ActivityMainBinding

public class MainActivity : AppCompatActivity(), View.OnClickListener {

    var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding.root)

        //即使使用 binding 也一定要用本语句
        binding.btnTestyufa?.setOnClickListener(this)
        binding.btnViewbindnig?.setOnClickListener(this)
        binding.btnCoroutline1?.setOnClickListener(this)
        binding.btnCoroutline2?.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when (v) {
            binding.btnTestyufa -> {
                binding.tvShowmsg  ?.append("测试语法！")
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


            binding.btnViewbindnig -> startActivity(Intent(this, ViewBindTestActivity::class.java))

            binding.btnCoroutline1 -> startActivity(Intent(this, CoroutineTestActivity1::class.java))


            binding.btnCoroutline2 -> startActivity(Intent(this, CoroutineTestActivity2::class.java))
        }
    }

}