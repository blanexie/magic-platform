package com.github.blanexie.magic.files.service

import cn.hutool.cache.CacheListener
import cn.hutool.cache.CacheUtil
import cn.hutool.core.convert.Convert
import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.core.util.URLUtil
import com.github.blanexie.magic.files.config.FileProperties
import com.github.blanexie.magic.files.util.ContentTypeUtil
import jakarta.servlet.http.HttpServletResponse.SC_OK
import jakarta.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.util.*
import kotlin.io.path.*


@Service
class FileService(
        final val fileProperties: FileProperties
) {
    val log = LoggerFactory.getLogger(FileService::class.java)
    val rangeRegex = Regex("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")
    val html = "<!DOCTYPE html><html><head><title>{title}</title></head><body>{dirList}</body></html>"
    final val fileCache = CacheUtil.newLFUCache<String, FileChannel>(5, 10 * 60 * 1000L)
    final var home: String = ""
    final var rangeSize: Long = 0

    init {
        val listener = CacheListener { _: String, fileChannel: FileChannel ->
            fileChannel.close()
        }
        fileCache.setListener(listener)
        this.home = fileProperties.home!!
        this.rangeSize = fileProperties.rangeSize
    }

    fun read(path: Path, range: String?): Result {
        log.info("请求的文件路径：{} range:{}", path.pathString, range)
        return if (path.isDirectory()) {
            readDir(path)
        } else {
            readFile(path, range)
        }
    }

    fun readDir(path: Path): Result {
        val bytes = readDirBytes(path)
        val headers = mapOf(
                "Content-Type" to "text/html; charset=UTF-8",
                "Content-Length" to bytes.size,
        )
        return Result(SC_OK, headers, bytes)
    }

    fun readFile(path: Path, range: String?): Result {
        log.info("进入文件下载， patH:{} range:{}", path, range)
        val file = path.toFile()
        val total = FileUtil.size(file)
        val contentType = ContentTypeUtil.getContentTypeByExtension(path.extension)
        val headers = mutableMapOf<String, Any>("Content-Type" to contentType)
        headers["Age"] = (System.currentTimeMillis() - file.lastModified()) / 1000
        headers["Cache-Control"] = 2592000
        headers["Etag"] = path.pathString.hashCode()
        headers["Last-Modified"] = Date(path.toFile().lastModified())
        //客户端没有发送range（传入格式不对也算没传）
        if (range == null || !rangeRegex.matches(range)) {
            return buildNotRangeResult(total, headers, file)
        }
        //解析客户端传入的Range请求头的数据， 并计算出start和end
        val pair = parseRangeHeader(range ?: "", total)
        //获取本次要读取的字节数组
        val bytes = readRangeBytes(file, pair.first, pair.second)
        log.info("读取了分块的字节数组， size:{} start:{} end:{} total:{}", bytes.size, pair.first, pair.second, total)
        headers["Content-Range"] = "bytes ${pair.first}-${pair.second}/$total"
        headers["Accept-Ranges"] = "bytes"
        headers["Content-Length"] = bytes.size
        return Result(SC_PARTIAL_CONTENT, headers, bytes)
    }

    private fun buildNotRangeResult(total: Long, headers: MutableMap<String, Any>, file: File): Result {
        return if (total > rangeSize) {
            headers["Accept-Ranges"] = "bytes"
            Result(SC_OK, headers, null)
        } else {
            Result(SC_OK, headers, FileUtil.readBytes(file))
        }
    }

    /**
     * 读取目录的内容
     */
    private fun readDirBytes(path: Path): ByteArray {
        val home = Path(home)
        //遍历该目录的所有子集,逐个拼接li标签， 最后连接所有的标签
        val dirs = path.listDirectoryEntries().map {
            val href = it.subpath(home.nameCount, it.nameCount).toList().joinToString(separator = "/") { p ->
                URLUtil.encode(p.name)
            }
            "<li><a href='/files/$href'>${it.name}</a></li>"
        }.joinToString(separator = "") { it }
        //拼接上一级跳转的li标签
        val dirList = "<ul><li><a href='..'>..</a></li>$dirs</ul>"
        //拼接完成的网页
        val html = StrUtil.format(html, mapOf("title" to path.name, "dirList" to dirList))
        //转换成字节数组， 用于响应给浏览器
        return html.toByteArray()
    }

    @Synchronized
    private fun readRangeBytes(file: File, start: Long, end: Long): ByteArray {
        val rangeSize = (end - start + 1).toInt()
        val buffer = ByteBuffer.allocate(rangeSize)
        val fileChannel = fileCache.get(file.path) {
            RandomAccessFile(file, "r").channel
        }
        // 设置文件通道的位置到该线程负责的起始位置
        fileChannel.position(start)
        fileChannel.read(buffer)
        // 处理读取到的数据
        buffer.flip()
        val byteArray = buffer.array()
        return byteArray
    }

    private fun parseRangeHeader(range: String, total: Long): Pair<Long, Long> {
        val byteRanges = range.replace("bytes=", "").split(",").first()
                .split("-").mapNotNull { Convert.toLong(it) }
        val start = byteRanges.getOrElse(0) { 0L }
        var end = byteRanges.getOrElse(1) { if (total > rangeSize) start + rangeSize - 1 else total - 1 }
        //检查客户端请求的范围，超出了块大小， 修改下end值，限制在rangeSize范围内
        val reqRangeSize = end - start + 1
        if (reqRangeSize > rangeSize) {
            end = start + rangeSize - 1
        }
        return Pair(start, end)
    }
}


data class Result(
        val status: Int,
        val headers: Map<String, *>,
        val bytes: ByteArray?,
)

