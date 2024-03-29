package top.fksoft.execute

import jdkUtils.ModConfig
import jdkUtils.logcat.Logger
import top.fksoft.server.http.HttpServer
import java.io.File

object Main {
    private val logger = Logger.getLogger(Main::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val httpServer = HttpServer(8080)
            val serverConfig = httpServer.serverConfig
            ModConfig.debug = true
            serverConfig.workDirectory = File("D:\\")
//            serverConfig.addAutoHttpServlet(InfoServlet::class)
//            serverConfig.addHttpServletBinder(ResServletBinder("/","/InfoHtml.html"))
//            serverConfig.addHttpServletBinder(ResServletBinder("/marked.min.js","/marked.min.js"))
            httpServer.start()
            var breakIt = false
            while (!breakIt){
                readLine()?.let {
                    val line = it.trim().toUpperCase()
                    if (line == "STOP"){
                        println("Stop Service ...")
                        breakIt = true
                        httpServer.close()
                    }else{
                        println("Unknown Command.")
                    }
                }

            }
        } catch (e: Exception) {
            logger.error("启动服务器时出现问题 ！", e)
        }

    }
}

