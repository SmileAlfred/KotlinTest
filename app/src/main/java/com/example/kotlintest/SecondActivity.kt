package com.example.kotlintest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kotlintest.databinding.ActivitySecondBinding

/**
 * 尝试使用 VIewBindig
 * 1. build.gradle 先配置；
 */
class SecondActivity : AppCompatActivity() {
    //2. 这里对应了 xml 文件名；
    var _binding: ActivitySecondBinding? = null
    //3. 定义 binding ；不可省略
    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        //4. 本句不必删除
        super.onCreate(savedInstanceState)
        //5. 根据 xml 文件名示例化
        _binding = ActivitySecondBinding.inflate(getLayoutInflater());
        //6. 仍然要 setContentView()
        setContentView(binding.root)
        //7. 使用 . 即可调用其控件，要注意其 id 名 一一对应

        binding.imageButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "尝试使用 ViewBinding", Toast.LENGTH_SHORT).show()
        })
    }
}