package com.jackiepenghe.baselibraryforkotlin.tools

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.jackiepenghe.baselibraryforkotlin.exception.WrongByteArrayLengthException
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.*
import kotlin.experimental.and

@Suppress("unused", "MemberVisibilityCanBePrivate")
/**
 * 工具类
 *
 * @author alm
 */

object Tool {

    /**
     * 设备地址的长度
     */
    private const val ADDRESS_BYTE_LENGTH = 6

    /**
     * log的TAG
     */
    private var TAG = "BaseLibrary->"
    /**
     * 是否打印日志信息的标志
     */
    /**
     * 获取当前日志打印标志
     *
     * @return 日志打印标志
     */
    private var isDebug = false

    /**
     * 是否重用未消失的Toast
     */
    var isToastReuse: Boolean
        get() = ToastHandler.isReuse
        set(reuse) = CustomToast.setReuse(reuse)

    /**
     * 检测系统环境是否是中文简体
     *
     * @return true表示为中文简体
     */
    val isZhCN: Boolean
        get() {
            val aDefault = Locale.getDefault()
            val aDefaultStr = aDefault.toString()
            val zhCn = "zh_CN"
            return zhCn == aDefaultStr
        }

    /**
     * 设置日志打印标志
     *
     * @param debug 日志打印标志
     */
    fun setDebugFlag(debug: Boolean) {
        isDebug = debug
    }

    /**
     * 设置默认的tag
     *
     * @param tag 默认的tag
     */
    fun setDefaultTAG(tag: String) {
        TAG = tag
    }

    /**
     * 等同于Log.i
     *
     * @param tag     tag
     * @param message 日志信息
     */
    fun infoOut(tag: String, message: String) {
        if (!isDebug) {
            return
        }
        Log.i(TAG + tag, message)
    }

    /**
     * 等同于Log.i
     *
     * @param message 日志信息
     */
    fun infoOut(message: String) {
        infoOut(TAG, message)
    }

    /**
     * 等同于Log.e
     *
     * @param tag     tag
     * @param message 日志信息
     */
    fun errorOut(tag: String, message: String) {
        if (!isDebug) {
            return
        }
        Log.e(TAG + tag, message)
    }

    /**
     * 等同于Log.e
     *
     * @param message 日志信息
     */
    fun errorOut(message: String) {
        errorOut(TAG, message)
    }

    /**
     * 等同于Log.d
     *
     * @param tag     tag
     * @param message 日志信息
     */
    fun debugOut(tag: String, message: String) {
        if (!isDebug) {
            return
        }
        Log.d(TAG + tag, message)
    }

    /**
     * 等同于Log.d
     *
     * @param message 日志信息
     */
    fun debugOut(message: String) {
        debugOut(TAG, message)
    }

    /**
     * 等同于Log.w
     *
     * @param tag     tag
     * @param message 日志信息
     */
    fun warnOut(tag: String, message: String) {
        if (!isDebug) {
            return
        }
        Log.w(TAG + tag, message)
    }

    /**
     * 等同于Log.w
     *
     * @param message 日志信息
     */
    fun warnOut(message: String) {
        warnOut(TAG, message)
    }


    /**
     * 等同于Log.v
     *
     * @param tag     tag
     * @param message 日志信息
     */
    fun verOut(tag: String, message: String) {
        if (!isDebug) {
            return
        }
        Log.v(TAG + tag, message)
    }

    /**
     * 等同于Log.v
     *
     * @param message 日志信息
     */
    fun verOut(message: String) {
        verOut(TAG, message)
    }

    /**
     * 弹出Toast
     *
     * @param context  上下文
     * @param message  信息
     * @param duration 持续时间
     */
    private fun showToast(context: Context, message: String, duration: Int) {
        CustomToast.makeText(context, message, duration).show()
    }

    /**
     * 弹出Toast
     *
     * @param context    上下文
     * @param messageRes 信息
     * @param duration   持续时间
     */
    private fun showToast(context: Context, @StringRes messageRes: Int, duration: Int) {
        CustomToast.makeText(context, messageRes, duration).show()
    }

    /**
     * 长时间的吐司
     *
     * @param context 上下文
     * @param message 信息
     */
    fun toastL(context: Context, message: String) {
        showToast(context, message, CustomToast.LENGTH_LONG)
    }

    /**
     * 长时间的吐司
     *
     * @param context    上下文
     * @param messageRes 信息
     */
    fun toastL(context: Context, @StringRes messageRes: Int) {
        showToast(context, messageRes, CustomToast.LENGTH_LONG)
    }

    /**
     * 短时间的吐司
     *
     * @param context 上下文
     * @param message 信息
     */
    fun toastS(context: Context, message: String) {
        showToast(context, message, CustomToast.LENGTH_SHORT)
    }

    /**
     * 短时间的吐司
     *
     * @param context    上下文
     * @param messageRes 信息
     */
    fun toastS(context: Context, @StringRes messageRes: Int) {
        showToast(context, messageRes, CustomToast.LENGTH_SHORT)
    }

    /**
     * 自定义时间的吐司
     *
     * @param context 上下文
     * @param message 信息
     */
    fun toast(context: Context, message: String, duration: Int) {
        showToast(context, message, duration)
    }

    /**
     * 自定义时间的吐司
     *
     * @param context    上下文
     * @param messageRes 信息
     */
    fun toast(context: Context, @StringRes messageRes: Int, duration: Int) {
        showToast(context, messageRes, duration)
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    fun strToHexStr(str: String): String {

        val chars = "0123456789ABCDEF".toCharArray()
        val sb = StringBuilder()
        val bs = str.toByteArray()
        var bit: Int

        for (b in bs) {
            bit = (b and 0x0f0.toByte()).toInt().ushr(4)
            sb.append(chars[bit])
            bit = (b and 0x0f.toByte()).toInt()
            sb.append(chars[bit])
            sb.append(' ')
        }
        return sb.toString().trim { it <= ' ' }
    }

    /**
     * 字符串转换成byte数组（数组长度最长为bytesLength）
     *
     * @param s           要转换成byte[]的字符串
     * @param bytesLength 数组长度的最大值（数组长度超过该值会被截取，长度不足该值为数组原长度）
     * @return 转换后获得的byte[]
     */
    fun getBytes(s: String, bytesLength: Int): ByteArray? {
        return getBytes(s, Charset.defaultCharset(), bytesLength)
    }

    /**
     * 字符串转换成byte数组（数组长度最长为bytesLength）
     *
     * @param s           要转换成byte[]的字符串
     * @param charsetName 编码方式的名字
     * @param bytesLength 数组长度的最大值（数组长度超过该值会被截取，长度不足该值为数组原长度）
     * @return 转换后获得的byte[]
     * @throws UnsupportedCharsetException 不支持的编码类型
     */
    @Throws(UnsupportedCharsetException::class)
    fun getBytes(s: String, charsetName: String, bytesLength: Int): ByteArray? {
        val charset = Charset.forName(charsetName)
        return getBytes(s, charset, bytesLength)
    }

    /**
     * 字符串转换成byte数组（数组长度最长为bytesLength）
     *
     * @param s           要转换成byte[]的字符串
     * @param charset     编码方式
     * @param bytesLength 数组长度的最大值（数组长度超过该值会被截取，长度不足该值为数组原长度）
     * @return 转换后获得的byte[]
     */
    fun getBytes(s: String?, charset: Charset, bytesLength: Int): ByteArray? {
        if (s == null) {
            return null
        }
        if (bytesLength < 0) {
            throw UnsupportedOperationException("bytesLength cannot be negative")
        }
        val data: ByteArray
        if (bytesLength > 0) {
            if (s.length > bytesLength) {
                data = ByteArray(bytesLength)
                System.arraycopy(s.toByteArray(charset), 0, data, 0, bytesLength)
            } else {
                data = s.toByteArray(charset)
            }
        } else {
            data = s.toByteArray(charset)
        }
        return data
    }

    /**
     * 字符串转换成byte数组，自动判断中文简体语言环境，在中文简体下，自动以GBK方式转换（数组长度最长为bytesLength）
     *
     * @param s           要转换成byte[]的字符串
     * @param bytesLength 数组长度的最大值（数组长度超过该值会被截取，长度不足该值为数组原长度）
     * @return 转换后获得的byte[]
     */
    fun getBytesAutoGBK(s: String, bytesLength: Int): ByteArray? {
        return if (isZhCN) {
            getBytes(s, "GBK", bytesLength)
        } else {
            getBytes(s, bytesLength)
        }
    }

    /**
     * 十六进制转换字符串
     *
     * @param hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    fun hexStrToStr(hexStr: String): String {
        val str = "0123456789ABCDEF"
        val hexs = hexStr.toCharArray()
        val bytes = ByteArray(hexStr.length / 2)
        var n: Int

        for (i in bytes.indices) {
            n = str.indexOf(hexs[2 * i]) * 16
            n += str.indexOf(hexs[2 * i + 1])
            bytes[i] = (n and 0xff).toByte()
        }
        return String(bytes)
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param bytes byte数组
     * @return String 每个Byte值之间空格分隔
     */
    fun bytesToHexStr(bytes: ByteArray): String {
        var stmp: String
        val sb = StringBuilder()
        for (aByte in bytes) {
            stmp = Integer.toHexString((aByte and 0xFF.toByte()).toInt())
            sb.append(if (stmp.length == 1) "0$stmp" else stmp)
            sb.append(" ")
        }
        return sb.toString().toUpperCase().trim { it <= ' ' }
    }

    /**
     * 将长整形转为byte数组
     *
     * @param value 长整形
     * @return byte数组
     */
    fun longToBytes(value: Long): ByteArray {

        var hexString = java.lang.Long.toHexString(value)
        val length = hexString.length
        if (length % 2 == 0) {
            val bytes = ByteArray(length / 2)
            for (i in bytes.indices) {
                val cacheString = hexString.substring(i * 2, i * 2 + 2)
                Tool.warnOut(TAG, "cacheString = $cacheString")
                val cache = java.lang.Short.parseShort(cacheString, 16)
                bytes[i] = cache.toByte()
            }
            return bytes
        } else {
            val bytes = ByteArray(length / 2 + 1)
            val substring = hexString.substring(0, 1)
            bytes[0] = java.lang.Short.parseShort(substring, 16).toByte()
            hexString = hexString.substring(1)
            for (i in 0 until bytes.size - 1) {
                val cacheString = hexString.substring(i * 2, i * 2 + 2)
                Tool.warnOut(TAG, "cacheString = $cacheString")
                val cache = java.lang.Short.parseShort(cacheString, 16)
                bytes[i + 1] = cache.toByte()
            }
            return bytes
        }
    }

    /**
     * bytes字符串转换为Byte值
     *
     * @param src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    @Throws(NumberFormatException::class)
    fun hexStrToBytes(src: String): ByteArray {
        var m: Int
        var n: Int
        val l = src.length / 2
        println(l)
        val ret = ByteArray(l)
        for (i in 0 until l) {
            m = i * 2 + 1
            n = m + 1
            val integer = Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n))
            ret[i] = integer.toByte()
        }
        return ret
    }

    /**
     * String的字符串转换成unicode的String
     *
     * @param strText 全角字符串
     * @return String 每个unicode之间无分隔符
     */
    fun strToUnicode(strText: String): String {
        var c: Char
        val str = StringBuilder()
        var intAsc: Int
        var strHex: String
        for (i in 0 until strText.length) {
            c = strText[i]
            intAsc = c.toInt()
            strHex = Integer.toHexString(intAsc)
            if (intAsc > 128) {
                str.append("\\u").append(strHex)
            } else {
                str.append("\\u00").append(strHex)
            }// 低位在前面补00
        }
        return str.toString()
    }

    /**
     * unicode的String转换成String的字符串
     *
     * @param hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    fun unicodeToString(hex: String): String {
        val t = hex.length / 6
        val str = StringBuilder()
        for (i in 0 until t) {
            val s = hex.substring(i * 6, (i + 1) * 6)
            // 高位需要补上00再转
            val s1 = s.substring(2, 4) + "00"
            // 低位直接转
            val s2 = s.substring(4)
            // 将16进制的string转为int
            val n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16)
            // 将int转换为字符
            val chars = Character.toChars(n)
            str.append(String(chars))
        }
        return str.toString()
    }

    //    /**
    //     * 将高位的byte和低位的byte拼接成一个int
    //     *
    //     * @param highByte 高位的byte
    //     * @param lowByte  低位的byte
    //     * @return 拼接好的int
    //     */
    //    @SuppressWarnings("unused")
    //    public static int bytesToInt(byte highByte, byte lowByte) {
    //        byte[] bytes = new byte[2];
    //        bytes[0] = highByte;
    //        bytes[1] = lowByte;
    //        String s = bytesToHexStr(bytes);
    //        String[] split = s.split(" ");
    //        int high = Integer.parseInt(split[0], 16);
    //        int low = Integer.parseInt(split[1], 16);
    //        return high * 256 + low;
    //    }

    /**
     * 将一个byte数组拼接为一个int型数
     *
     * @param bytes byte数组长度不超过4
     * @return int型数
     */
    fun bytesToInt(bytes: ByteArray): Int {
        val cache0: Byte
        val cache1: Byte
        val cache2: Byte
        val cache3: Byte

        val value0: Int
        val value1: Int
        val value2: Int
        val value3: Int
        val length = bytes.size
        when (length) {
            1 -> {
                cache0 = bytes[0]
                return (0x00FF.toByte() and cache0).toInt()
            }
            2 -> {
                cache0 = bytes[0]
                cache1 = bytes[1]
                value0 = cache0.toInt() shl 8
                value1 = cache1.toInt()
                return value0 or value1
            }
            3 -> {
                cache0 = bytes[0]
                cache1 = bytes[1]
                cache2 = bytes[2]
                value0 = cache0.toInt() shl 16
                value1 = cache1.toInt() shl 8
                value2 = cache2.toInt()
                return value0 or value1 or value2
            }
            4 -> {
                cache0 = bytes[0]
                cache1 = bytes[1]
                cache2 = bytes[2]
                cache3 = bytes[3]
                value0 = cache0.toInt() shl 24
                value1 = cache1.toInt() shl 16
                value2 = cache2.toInt() shl 8
                value3 = cache3.toInt()
                return value0 or value1 or value2 or value3
            }
            else -> throw WrongByteArrayLengthException("byte array length must be less than 4")
        }
    }

    fun twoBytesToInt(bytes: ByteArray): Int {
        if (bytes.size != 2) {
            throw WrongByteArrayLengthException("byte array length must be 2")
        }
        val s = bytesToHexStr(bytes)
        val split = s.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val high = Integer.parseInt(split[0], 16)
        val low = Integer.parseInt(split[1], 16)
        return high * 256 + low
    }


    /**
     * 将一个整数转换成2个字节的byte数组
     *
     * @param i 整数
     * @return 2个字节的byte数组
     */
    fun intToBytesLength2(i: Int): ByteArray {
        val hexString = intToHexStr(i)
        val highByte: Byte
        val lowByte: Byte
        val hexStringMinLength = 2
        if (hexString.length > hexStringMinLength) {
            var substring = hexString.substring(0, hexString.length - 2)
            highByte = Integer.parseInt(substring, 16).toByte()
            substring = hexString.substring(hexString.length - 2, hexString.length)
            lowByte = Integer.parseInt(substring, 16).toByte()
        } else {
            highByte = 0
            lowByte = Integer.parseInt(hexString, 16).toByte()
        }
        return byteArrayOf(highByte, lowByte)
    }

    /**
     * 将一个整数转换成4个字节的byte数组
     *
     * @param n 整数
     * @return 4个字节的byte数组
     */
    fun intToBytesLength4(n: Int): ByteArray {
        val b = ByteArray(4)
        for (i in b.indices) {
            b[i] = n.ushr(24 - i * 8).toByte()
        }
        return b
    }

    /**
     * 将整数转换成16进制字符串
     *
     * @param i 整数
     * @return 16进制字符串
     */
    fun intToHexStr(i: Int): String {
        return Integer.toHexString(i)
    }

    /*---------------------------------其他--------------------------*/

    /**
     * 让当前线程阻塞一段时间
     *
     * @param timeMillis 让线程阻塞的时间（单位：毫秒）
     */
    fun sleep(timeMillis: Long) {
        val currentTimeMillis = System.currentTimeMillis()
        while (true) {
            if (System.currentTimeMillis() - currentTimeMillis >= timeMillis) {
                break
            }
        }
    }

    /**
     * 将字节型数据转换为0~255 (0xFF 即BYTE)
     *
     * @param data data字节型数据
     * *
     * @return 无符号的整型
     */
    fun getUnsignedByte(data: Byte): Int {
        return data.toInt() and 0x0FF
    }

    /**
     * 将字节型数据转换为0~65535 (0xFFFF 即 WORD)
     *
     * @param data 字节型数据
     * @return 无符号的整型
     */
    fun getUnsignedByte(data: Short): Int {
        return data.toInt() and 0x0FFFF
    }

    /**
     * 将int数据转换为0~4294967295 (0xFFFFFFFF即DWORD)。
     *
     * @param data int数据
     * @return 无符号的长整型
     */
    fun getUnsignedInt(data: Int): Long {
        //获取最低位
        val lowBit = (1 and data).toByte().toInt()
        //无符号右移一位（无符号数）
        val i = data.ushr(1)
        //将右移之后的数强转为long之后重新左移回去
        val l = i.toLong() shl 1
        //重新加上低位的值
        return l + lowBit
    }

    /**
     * 将int转为boolean(0 = false ,1 = true)
     *
     * @param value int值
     * *
     * @return 对应的结果
     */
    fun intToBoolean(value: Int): Boolean {
        return when (value) {
            0 -> false
            1 -> true
            else -> throw RuntimeException("The error value $value")
        }
    }

    /**
     * 将boolean转为int(true = 1,false = 0)
     *
     * @param b boolean值
     * *
     * @return 对应的int值
     */
    fun booleanToInt(b: Boolean): Int {
        return if (b) {
            1
        } else {
            0
        }
    }

    /**
     * 使用代码触发home键的效果
     *
     * @param context 上下文
     */
    fun pressHomeButton(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
        // 注意:必须加上这句代码，否则就不是单例了
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(intent)
    }

    /**
     * 检测byte数组中的内容有效性（全0为无效）
     *
     * @param bytes byte数组
     * @return true表示有效
     */
    fun checkByteValid(bytes: ByteArray): Boolean {
        for (aByte in bytes) {
            if (aByte.toInt() != 0) {
                return true
            }
        }
        return false
    }

    /**
     * 解除输入法的内存泄漏bug
     *
     * @param activity Activity
     */
    fun releaseInputMethodManagerMemory(activity: Activity) {
        //解除输入法内存泄漏
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        try {
            val mCurRootViewField = InputMethodManager::class.java.getDeclaredField("mCurRootView")
            val mNextServedViewField = InputMethodManager::class.java.getDeclaredField("mNextServedView")
            val mServedViewField = InputMethodManager::class.java.getDeclaredField("mServedView")
            mCurRootViewField.isAccessible = true
            mNextServedViewField.isAccessible = true
            mServedViewField.isAccessible = true
            val mCurRootView = mCurRootViewField.get(inputMethodManager)
            if (null != mCurRootView) {
                val context = (mCurRootView as View).context
                if (context === activity) {
                    //将该对象设为null，破环GC引用链，防止输入法内存泄漏
                    mCurRootViewField.set(inputMethodManager, null)
                }
            }
            val mNextServedView = mNextServedViewField.get(inputMethodManager)
            if (null != mNextServedView) {
                val context = (mNextServedView as View).context
                if (activity === context) {
                    mNextServedViewField.set(inputMethodManager, null)
                }
            }
            val mServedView = mServedViewField.get(inputMethodManager)
            if (null != mServedView) {
                val context = (mServedView as View).context
                if (activity === context) {
                    mServedViewField.set(inputMethodManager, null)
                }
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    /**
     * 将任意对象转为byte数组
     *
     * @param o 任意对象
     * @return byte数组
     */
    fun objectToByteArray(o: Any): ByteArray? {
        var bytes: ByteArray? = null
        val byteArrayOutputStream = ByteArrayOutputStream()
        var objectOutputStream: ObjectOutputStream? = null
        try {
            objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
            objectOutputStream.writeObject(o)
            bytes = byteArrayOutputStream.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        try {
            byteArrayOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bytes
    }

    /**
     * 将数组类型转为指定的对象
     *
     * @param bytes 数组类
     * @return T 指定对象
     */
    fun <T> byteArrayToObject(bytes: ByteArray?): T? {
        if (bytes == null) {
            return null
        }
        var o: Any? = null
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var objectInputStream: ObjectInputStream? = null
        try {
            objectInputStream = ObjectInputStream(byteArrayInputStream)
            o = objectInputStream.readObject()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        try {
            byteArrayInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        @Suppress("UNCHECKED_CAST")
        return o as T?
    }

    /**
     * 将蓝牙设备地址转为byte数组
     *
     * @param address 设备地址
     * @return byte数组
     */
    fun bluetoothAddressStringToByteArray(address: String?): ByteArray? {
        if (address == null) {
            return null
        }

        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            return null
        }

        val cacheArray = address.split(":".toRegex(), 6).toTypedArray()
        val bluetoothByteArray = ByteArray(6)

        for (i in cacheArray.indices) {
            val cache = cacheArray[i]
            val integer: Int?
            try {
                integer = Integer.valueOf(cache, 16)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return null
            }

            bluetoothByteArray[i] = integer!!.toByte()
        }
        return bluetoothByteArray
    }

    /**
     * 将设备地址数组转为设备地址字符串
     *
     * @param addressByteArray 设备地址数组
     * @return 设备地址字符串（AA:AA:AA:AA:AA:AA）
     */
    fun bluetoothAddressByteArrayToString(addressByteArray: ByteArray?): String? {
        if (addressByteArray == null) {
            return null
        }
        if (addressByteArray.size != ADDRESS_BYTE_LENGTH) {
            return null
        }

        val addressCacheString = Tool.bytesToHexStr(addressByteArray)
        val addressCache = addressCacheString.replace(" ", ":")
        return addressCache.toUpperCase()
    }

    /**
     * 将一个int型的Ip地址转为点分式地址字符串
     *
     * @param ip int型的Ip地址
     * @return 点分式字符串
     */
    fun intIp4ToStringIp4(ip: Int): String {
        return (ip and 0xFF).toString() + "." + (ip shr 8 and 0xFF) + "." + (ip shr 16 and 0xFF) + "." + (ip shr 24 and 0xFF)
    }

    /**
     * 将一个int型的Ip地址转为点分式地址字符串
     *
     * @param ip int型的Ip地址
     * @return 点分式字符串
     */
    fun intIp4ToReverseStringIp4(ip: Int): String {
        return ((ip shr 24 and 0xFF).toString() + "."
                + (ip shr 16 and 0xFF) + "."
                + (ip shr 8 and 0xFF) + "."
                + (ip and 0xFF))
    }
}
