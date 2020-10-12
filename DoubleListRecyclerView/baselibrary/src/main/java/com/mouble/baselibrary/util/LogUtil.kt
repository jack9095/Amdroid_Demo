package com.mouble.baselibrary.util

import android.content.Context
import android.os.Environment
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

object LogUtil {
    private var mLogType = TYPE.V
    private var mGlobalTag = "LogUtil"
    private var mSwitchLog = true
    private var mLogBorder = false
    private var dir = ""
    private var mFileName = "DefaultLog"

    private const val TOP_BORDER =
        "╔═══════════════════════════════════════════════════════════════════════════════════════════════════"
    //    private static final String LEFT_BORDER = "║ ";
    private const val LEFT_BORDER = ""
    private const val BOTTOM_BORDER =
        "╚═══════════════════════════════════════════════════════════════════════════════════════════════════"
    private val LINE_SEPARATOR = System.getProperty("line.separator")
    private const val NULL = "null"
    private const val ARGS = "args"
    const val LINE_MAX_WORD = 3000

    fun init(builder: Builder) {
        mSwitchLog = builder.mSwitchLog
        mGlobalTag = builder.mGlobalTag
        mLogType = builder.mLogType
        mLogBorder = builder.mLogBorder
        dir = builder.dir
        mFileName = builder.mFileName
    }

    fun v(contents: Any?) {
        log(TYPE.V, mGlobalTag, contents)
    }

    fun v(tag: String, vararg contents: Any?) {
        log(TYPE.V, tag, *contents)
    }

    fun d(contents: Any?) {
        log(TYPE.D, mGlobalTag, contents)
    }

    fun d(tag: String, vararg contents: Any?) {
        log(TYPE.D, tag, *contents)
    }

    fun i(contents: Any?) {
        log(TYPE.I, mGlobalTag, contents)
    }

    fun i(tag: String, vararg contents: Any?) {
        log(TYPE.I, tag, *contents)
    }

    fun w(contents: Any?) {
        log(TYPE.W, mGlobalTag, contents)
    }

    fun w(tag: String, vararg contents: Any?) {
        log(TYPE.W, tag, *contents)
    }

    fun e(contents: Any?) {
        log(TYPE.E, mGlobalTag, contents)
    }

    fun e(tag: String, vararg contents: Any?) {
        log(TYPE.E, tag, *contents)
    }

    fun a(contents: Any?) {
        log(TYPE.A, mGlobalTag, contents)
    }

    fun a(tag: String, vararg contents: Any?) {
        log(TYPE.A, tag, *contents)
    }

    fun file(contents: Any?) {
        log(TYPE.FILE, mGlobalTag, contents)
    }

    fun file(tag: String, contents: Any?) {
        log(TYPE.FILE, tag, contents)
    }

    fun file(fileName: String, tag: String, contents: Any?) {
        log(fileName, tag, contents)
    }


    fun json(contents: String?) {
        log(TYPE.JSON, mGlobalTag, contents)
    }

    fun json(tag: String, contents: String?) {
        log(TYPE.JSON, tag, contents)
    }

    fun xml(contents: String?) {
        log(TYPE.XML, mGlobalTag, contents)
    }

    fun xml(tag: String, contents: String?) {
        log(TYPE.XML, tag, contents)
    }

    private fun log(type: TYPE, tag: String, vararg contents: Any?) {
        if (!mSwitchLog) {
            return
        }
        val msg = processContents(type, *contents)
        when (type) {
            TYPE.V, TYPE.D, TYPE.I, TYPE.W, TYPE.E, TYPE.A -> realLog(type, tag, msg)
            TYPE.FILE -> logToFile(TYPE.E, tag, msg)
            TYPE.JSON -> realLog(TYPE.E, tag, msg)
            TYPE.XML -> realLog(TYPE.E, tag, msg)
            else -> {
            }
        }
    }

    // https://www.liangzl.com/get-article-detail-12859.html
    private fun log(fileName: String, tag: String, vararg contents: Any?) {
        if (!mSwitchLog) {
            return
        }
        val msg = processContents(TYPE.FILE, *contents)
        logToFile(TYPE.E, fileName, tag, msg)
    }

    private fun processContents(type: TYPE, vararg contents: Any?): String {
        val targetElement =
            Thread.currentThread().stackTrace[5]
        var className = targetElement.className
        val classNameInfo = className.split("\\.").toTypedArray()
        if (classNameInfo.isNotEmpty()) {
            className = classNameInfo[classNameInfo.size - 1]
        }
        if (className.contains("$")) {
            className = className.split("\\$").toTypedArray()[0]
        }
        //Log任务栈位置信息
        val head = Formatter()
            .format(
                "Thread: %s, %s(%s.java:%d)$LINE_SEPARATOR",
                Thread.currentThread().name,
                targetElement.methodName,
                className,
                targetElement.lineNumber
            )
            .toString()
        var msg = ""
        if (contents != null) { //只传入一个打印对象
            if (contents.size == 1) {
                val `object` = contents[0]
                msg = `object`?.toString() ?: NULL
                if (type == TYPE.JSON) {
                    msg = formatJson(msg)
                } else if (type == TYPE.XML) {
                    msg = formatXml(msg)
                }
            } else { //传入多个打印对象
                //需要表示出每个打印对象的顺序
                val sb = StringBuilder()
                var i = 0
                val len = contents.size
                while (i < len) {
                    val content = contents[i]
                    sb.append(ARGS)
                        .append("[")
                        .append(i)
                        .append("]")
                        .append(" = ")
                        .append(content?.toString() ?: NULL)
                        .append(LINE_SEPARATOR)
                    ++i
                }
                msg = sb.toString()
            }
        }
        if (mLogBorder) {
            val sb = StringBuilder()
            val lines = msg.split(LINE_SEPARATOR!!).toTypedArray()
            for (line in lines) {
                sb.append(LEFT_BORDER).append(line).append(LINE_SEPARATOR)
            }
            msg = sb.toString()
        }
        return head + msg
    }


    /**
     * 打印Log到文件中
     *
     * @param type
     * @param tag
     * @param msg
     */
    private fun logToFile(
        type: TYPE,
        fileName: String,
        tag: String,
        msg: String
    ) {
        synchronized(LogUtil::class.java) {
            try {
                val fullPath = "$dir$fileName.txt"
                val file = File(fullPath)
                if (!file.exists()) {
                    val newFile = file.createNewFile()
                    if (!newFile) {
                        log(type, tag, "create logFile failed!")
                        return
                    }
                }
                val time =
                    SimpleDateFormat("MM-dd HH:mm:ss.SSS ").format(Date())
                val sb = StringBuilder()
                if (mLogBorder) {
                    sb.append(TOP_BORDER).append(LINE_SEPARATOR)
                }
                sb.append(time)
                    .append(tag)
                    .append(": ")
                    .append(msg)
                    .append(LINE_SEPARATOR)
                if (mLogBorder) {
                    sb.append(BOTTOM_BORDER).append(LINE_SEPARATOR)
                }
                val writeMsg = sb.toString()
                object : Thread() {
                    override fun run() {
                        var bw: BufferedWriter? = null
                        try {
                            bw = BufferedWriter(FileWriter(file))
                            bw.write(writeMsg, 0, writeMsg.length)
                            bw.flush()
                            log(type, tag, "log into file success!")
                        } catch (e: IOException) {
                            log(type, tag, "log into file failed!")
                            e.printStackTrace()
                        } finally {
                            if (bw != null) {
                                try {
                                    bw.close()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }.start()
            } catch (e: IOException) {
                log(type, tag, "write to file failed!")
                e.printStackTrace()
            }
        }
    }

    /**
     * 打印Log到文件中
     *
     * @param type
     * @param tag
     * @param msg
     */
    private fun logToFile(type: TYPE, tag: String, msg: String) {
        logToFile(type, mFileName, tag, msg)
    }

    /**
     * 打印Log
     *
     * @param type
     * @param tag
     * @param msg
     */
    private fun realLog(type: TYPE, tag: String, msg: String) {
        if (mLogBorder) {
            logBorder(type, tag, true)
        }
        val length = msg.length
        val count = length / LINE_MAX_WORD
        if (count > 0) {
            var index = 0
            for (i in 0 until count) {
                printLog(type, tag, msg.substring(index, index + LINE_MAX_WORD))
                index += LINE_MAX_WORD
            }
            printLog(type, tag, msg.substring(index, length))
        } else {
            printLog(type, tag, msg)
        }
        if (mLogBorder) {
            logBorder(type, tag, false)
        }
    }

    private fun printLog(type: TYPE, tag: String, msg: String) {
        var msg = msg
        if (mLogBorder) {
            msg = LEFT_BORDER + msg
        }
        when (type) {
            TYPE.V -> Log.v(tag, msg)
            TYPE.D -> Log.d(tag, msg)
            TYPE.I -> Log.i(tag, msg)
            TYPE.W -> Log.w(tag, msg)
            TYPE.E -> Log.e(tag, msg)
            TYPE.A -> Log.wtf(tag, msg)
            else -> {
            }
        }
    }

    /**
     * 打印上下边框
     *
     * @param type
     * @param tag
     * @param isTop
     */
    private fun logBorder(type: TYPE, tag: String, isTop: Boolean) {
        val border = if (isTop) TOP_BORDER else BOTTOM_BORDER
        when (type) {
            TYPE.V -> Log.v(tag, border)
            TYPE.D -> Log.d(tag, border)
            TYPE.I -> Log.i(tag, border)
            TYPE.W -> Log.w(tag, border)
            TYPE.E -> Log.e(tag, border)
            TYPE.A -> Log.wtf(tag, border)
            else -> {
            }
        }
    }


    /**
     * Json字符串格式转换
     *
     * @param json
     * @return
     */
    private fun formatJson(json: String): String {
        var json = json
        try {
            if (json.startsWith("{")) {
                json = JSONObject(json).toString(4)
            } else if (json.startsWith("[")) {
                json = JSONArray(json).toString(4)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return json
    }

    /**
     * Xml格式字符串转换
     *
     * @param xml
     * @return
     */
    private fun formatXml(xml: String): String {
        var xml = xml
        try {
            val xmlInput: Source =
                StreamSource(StringReader(xml))
            val xmlOutput =
                StreamResult(StringWriter())
            val transformer =
                TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
            transformer.transform(xmlInput, xmlOutput)
            xml = xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">$LINE_SEPARATOR")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return xml
    }


    /**
     * 打印过滤器
     */
    enum class TYPE {
        V,  //Verbose
        D,  // Debug
        I,  //Info
        W,  // Warm
        E,  // Error
        A,  // Assert
        FILE,  // Write to File
        JSON,  // Parse Json
        XML //Parse XML
    }

    /**
     * Log 配置方法
     * -------------------------------------------------------
     */
    class Builder(context: Context) {
        internal var dir = ""
        internal var mSwitchLog = true
        internal var mGlobalTag = "LogUtil"
        internal var mLogBorder = true
        internal var mLogType = TYPE.V
        internal var mFileName = "DefaultLog"
        /**
         * 设置是否开启log
         *
         * @param isLog
         * @return
         */
        fun isLog(isLog: Boolean): Builder {
            this.mSwitchLog = isLog
            return this
        }

        /**
         * 全局的Log 标签
         *
         * @param tag
         * @return
         */
        fun setTag(tag: String): Builder {
            this.mGlobalTag = tag
            return this
        }

        /**
         * log是否带标签
         *
         * @param isLogBorder
         * @return
         */
        fun isLogBorder(isLogBorder: Boolean): Builder {
            this.mLogBorder = isLogBorder
            return this
        }

        /**
         * 设置Log过滤器
         *
         * @param type
         * @return
         */
        fun setLogType(type: TYPE): Builder {
            this.mLogType = type
            return this
        }

        /**
         * 自定义Log写入文件路径
         *
         * @param dirPath
         * @return
         */
        fun setLogFileDir(dirPath: String): Builder {
            this.dir = dirPath
            return this
        }

        /**
         * 设置全局的log写入目标文件
         *
         * @param fileName
         * @return
         */
        fun setLogFileName(fileName: String): Builder {
            this.mFileName = fileName
            return this
        }

        /**
         * 上下文设置
         *
         * @param context
         */
        init { //判断外存设备是否就绪
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                this.dir =
                    context.externalCacheDir.toString() + File.separator + "log" + File.separator
            } else {
                this.dir =
                    context.cacheDir.toString() + File.separator + "log" + File.separator
            }
        }
    }
}