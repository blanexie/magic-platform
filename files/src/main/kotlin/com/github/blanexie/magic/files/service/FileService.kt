package com.github.blanexie.magic.files.service

import cn.hutool.cache.CacheListener
import cn.hutool.cache.CacheUtil
import cn.hutool.core.convert.Convert
import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.core.util.URLUtil
import com.github.blanexie.magic.files.util.ContentTypeUtil
import com.github.blanexie.magic.files.util.Range
import jakarta.servlet.http.HttpServletResponse.SC_OK
import jakarta.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Path
import kotlin.io.path.*


@Service
class FileService(
        @Value("\${file.range.size}")
        val rangeSize: Long,
        @Value("\${file.home}")
        val home: String,
) {

    final val log: Logger = LoggerFactory.getLogger(FileService::class.java)
    val rangeRegex = Regex("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")
    val html = "<!DOCTYPE html><html><head><title>{title}</title></head><body>{dirList}</body></html>"
    final val fileCache = CacheUtil.newLFUCache<String, RandomAccessFile>(5, 10 * 60 * 1000L)

    init {
        val listener = CacheListener { _: String, randomAccessFile: RandomAccessFile ->
            randomAccessFile.close()
        }
        fileCache.setListener(listener)
    }

    fun read(path: Path, range: String?): Range {
        return if (path.isDirectory()) {
            readDir(path)
        } else {
            readFile(path, range)
        }
    }

    fun readDir(path: Path): Range {
        val bytes = readDirBytes(path)
        val headers = mapOf(
                "Content-Type" to "text/html; charset=UTF-8",
                "Content-Length" to bytes.size,
        )
        return Range(SC_OK, headers, bytes)
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
            "<li><a href='/$href'>${it.name}</a></li>"
        }.joinToString(separator = "") { it }
        //拼接上一级跳转的li标签
        val dirList = "<ul><li><a href='..'>..</a></li>$dirs</ul>"
        //拼接完成的网页
        val html = StrUtil.format(html, mapOf("title" to path.name, "dirList" to dirList))
        //转换成字节数组， 用于响应给浏览器
        return html.toByteArray()
    }

    fun readFile(path: Path, range: String?): Range {
        val file = path.toFile()
        val total = FileUtil.size(file)
        val contentType = ContentTypeUtil.getContentTypeByExtension(path.extension)
        val headers = mutableMapOf<String, Any>("Content-Length" to total, "Content-Type" to contentType)
        //客户端没有发送range（传入格式不对也算没传）
        if (range == null || !rangeRegex.matches(range)) {
            //客户端首次请求， 如果是小文件就直接返回前端， 如果是大文件就告知客户端需要使用range
            return buildNotRangeResult(total, headers, file)
        }
        //解析客户端传入的Range请求头的数据， 并计算出start和end
        val pair = parseRangeHeader(range, total)
        //获取本次要读取的字节数组
        val bytes = readRangeBytes(file, pair.first, pair.second)
        //确认是否已经到达了结尾
        if (pair.second == total - 1) {
            //已经到达结尾了
            return Range(SC_OK, headers, bytes)
        } else {
            headers["Content-Range"] = "${pair.first}-${pair.second}/$total"
            headers["Accept-Ranges"] = "bytes"
            return Range(SC_PARTIAL_CONTENT, headers, bytes)
        }
    }

    private fun buildNotRangeResult(total: Long, headers: MutableMap<String, Any>, file: File): Range {
        return if (total > rangeSize) {
            headers["Accept-Ranges"] = "bytes"
            Range(SC_OK, headers, null)
        } else {
            Range(SC_OK, headers, FileUtil.readBytes(file))
        }
    }

    private fun readRangeBytes(file: File, start: Long, end: Long): ByteArray {
        val rAFile = fileCache.get(file.path) {
            RandomAccessFile(file, "r")
        }
        rAFile.seek(start)
        val byteArray = ByteArray((end - start + 1).toInt())
        rAFile.read(byteArray)
        return byteArray
    }


    private fun parseRangeHeader(range: String, total: Long): Pair<Long, Long> {
        val byteRanges = range.replace("bytes=", "").split(",").first()
                .split("-").mapNotNull { Convert.toLong(it) }
        val start = byteRanges.getOrElse(0) { 0L }
        var end = byteRanges.getOrElse(1) { if (total > rangeSize) start + rangeSize else total - 1 }
        //检查客户端请求的范围，超出了块大小， 修改下end值，限制在rangeSize范围内
        val reqRangeSize = end - start + 1
        if (reqRangeSize > rangeSize) {
            end = start + rangeSize
        }
        return Pair(start, end)
    }

}


