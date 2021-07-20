package com.example.kotlintest

import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*

object Utils {
    /**
     * 获取某个目录的可用空间
     */

    fun getAvailSpace(path: String): Long {

        val statfs = StatFs(path);

        var size: Long = statfs.getBlockSize().toLong();//获取分区的大小

        var count: Long = statfs.getAvailableBlocks().toLong();//获取可用分区块的个数

        return size * count;

    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun queryStorage() {
        val statFs: StatFs = StatFs(Environment.getExternalStorageDirectory().getPath());

        //存储块总数量
        val blockCount = statFs.getBlockCount();
        //块大小
        val blockSize = statFs.getBlockSize();
        //可用块数量
        val availableCount = statFs.getAvailableBlocks();
        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        val freeBlocks = statFs.getFreeBlocks();
        //这两个方法是直接输出总内存和可用空间，也有getFreeBytes
        //API level 18（JELLY_BEAN_MR2）引入
       val  totalSize:Long = statFs . getTotalBytes ();
        val availableSize:Long = statFs . getAvailableBytes ();

        Log.d("设备 ", "total = " + getUnit(totalSize));
        Log.d("设备 ", "availableSize = " + getUnit(availableSize));

        //这里可以看出 available 是小于 free ,free 包括保留块。
        Log.d("设备 ", "total = " + getUnit((blockSize as Long) * blockCount));
        Log.d("设备 ", "available = " + getUnit((blockSize)as Long * availableCount));
        Log.d("设备 ", "free = " + getUnit((blockSize)as Long * freeBlocks));
    }

    val units: Array<String> = arrayOf("B", "KB", "MB", "GB", "TB" )
    /**
     * 单位转换
     */
    fun getUnit(size: Long): String {
        var inputSize:Long = size
        var index = 0;
        while (inputSize > 1024 && index < 4) {
            inputSize = inputSize / 1024;
            index++;
        }
        return ""+inputSize+ units[index]
        //return String.format(Locale.getDefault(), " %.2f %s", size, units[index]);
    }
}