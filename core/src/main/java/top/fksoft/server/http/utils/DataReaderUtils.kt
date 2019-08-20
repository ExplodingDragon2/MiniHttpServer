package top.fksoft.server.http.utils

import top.fksoft.server.http.logcat.Logger
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class DataReaderUtils(private val inputStream: InputStream, private val charset: Charset = Charsets.UTF_8) {
    private val logger = Logger.getLogger(DataReaderUtils::class)
    init {
        if (inputStream.available() == 0) {
            throw IOException("无法读取到更多数据！")
        }
    }

    /**
     * # 读取一行数据
     * @return String
     */
    fun readLine(): String? {
        val defaultResult = ""
        if (inputStream.available() == 0)
            return null
        var charLine = 0
        val outputStream = ByteArrayOutputStream()
        var byteChar: Char
        var read: Int
        while (true) {
            if (inputStream.available() == 0) {
                break
            }
            read = inputStream.read()
            if (read == -1) {
                break
            }
            byteChar = read.toChar()
            if (byteChar == '\r' || byteChar == '\n') {
                charLine++
                if (charLine > 2) {
                    outputStream.reset()
                    break
                }
                if (outputStream.size() == 0) {
                    continue
                } else {
                    break
                }
            }
            outputStream.write(read)
        }
        val result = when (outputStream.size()) {
            0 -> defaultResult
            else -> {
                val byteArray = outputStream.toByteArray()
                byteArray?.let {
                    return String(it, charset)
                }
                return defaultResult
            }
        }
        outputStream.reset()
        outputStream.close()
        return result
    }

    /**
     * 将位置移动到下一行
     * @return Boolean
     */
    fun nextLine() :Boolean{
        return readLine() != null
    }


    fun copy(output: OutputStream, length: Int): Boolean{
        var result = false
        var len2 = 0
        try {
            val bytes = ByteArray(1024)
                while (true){
                    if (inputStream.available() == 0) {
                        return len2 >=length
                    }
                    val readSize = inputStream.read(bytes, 0, bytes.size)
                    len2 += readSize
                    output.write(bytes,0,readSize)
                    output.flush()
                }
                result = true
        }catch (e:Exception){
            logger.debug("copy error.",e)
        }
        return result
    }

}