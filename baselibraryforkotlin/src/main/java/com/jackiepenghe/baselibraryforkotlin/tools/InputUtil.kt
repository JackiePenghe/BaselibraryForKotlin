package com.jackiepenghe.baselibraryforkotlin.tools

import android.content.Context
import android.os.Environment
import com.jackiepenghe.baselibraryforkotlin.files.FileUtil


import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.OptionalDataException
import java.io.StreamCorruptedException

@Suppress("unused")
/**
 * 文件输入流工具类
 *
 * @author ALM Sound Technology
 */
class InputUtil<T> {

    /*--------------------------------公开函数--------------------------------*/

    /**
     * 读取本地对象
     *
     * @param context 上下文
     * @param fileName 文件名
     * @return 读取到的对象
     */
    fun readObjectFromLocal(context: Context, fileName: String): T? {
        val bean: T
        try {
            //获得输入流
            val fis = context.openFileInput(fileName)
            val ois = ObjectInputStream(fis)
            @Suppress("UNCHECKED_CAST")
            bean = ois.readObject() as T
            fis.close()
            ois.close()
            return bean
        } catch (e: StreamCorruptedException) {
            e.printStackTrace()
            return null
        } catch (e: OptionalDataException) {
            e.printStackTrace()
            return null
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 读取sd卡对象
     *
     * @param fileName 文件名
     * @return 泛型对象
     */
    fun readObjectFromSdCard(fileName: String): T? {
        //检测sd卡是否存在
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val bean: T
            val appDir = FileUtil.appDir
            val sdFile = File(appDir, fileName)
            try {
                val fis = FileInputStream(sdFile)
                val ois = ObjectInputStream(fis)
                @Suppress("UNCHECKED_CAST")
                bean = ois.readObject() as T
                fis.close()
                ois.close()
                return bean
            } catch (e: StreamCorruptedException) {
                e.printStackTrace()
                return null
            } catch (e: OptionalDataException) {
                e.printStackTrace()
                return null
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return null
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                return null
            }

        } else {
            return null
        }
    }

    /**
     * 读取sd卡对象
     *
     * @param fileName 文件名
     * @return 读取到的list
     */
    fun readListFromSdCard(fileName: String): List<T>? {
        //检测sd卡是否存在
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val list: List<T>
            val appDir = FileUtil.appDir

            val sdFile = File(appDir, fileName)
            try {
                val fis = FileInputStream(sdFile)
                val ois = ObjectInputStream(fis)
                @Suppress("UNCHECKED_CAST")
                list = ois.readObject() as List<T>
                fis.close()
                ois.close()
                return list
            } catch (e: StreamCorruptedException) {
                e.printStackTrace()
                return null
            } catch (e: OptionalDataException) {
                e.printStackTrace()
                return null
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return null
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                return null
            }

        } else {
            return null
        }
    }

}

