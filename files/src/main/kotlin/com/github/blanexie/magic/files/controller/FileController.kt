package com.github.blanexie.tserver.controller

import cn.hutool.core.convert.Convert
import cn.hutool.core.util.URLUtil
import com.github.blanexie.magic.files.config.FileProperties
import com.github.blanexie.magic.files.service.FileService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.nio.file.Path
import kotlin.io.path.exists

@Controller
@RequestMapping("/files")
class FileController(
        val fileService: FileService,
        val fileProperties: FileProperties
) {

    @GetMapping("/**")
    fun path(request: HttpServletRequest, response: HttpServletResponse) {
        //获取用户请求的文件
        val homePath = fileProperties.getHomePath()
        val path = homePath.resolve(URLUtil.decode(request.requestURI ?: ""))
        //校验用户请求的文件
        if (!checkFile(path, response)) {
            return
        }
        val range = request.getHeader("Range")
        val result = fileService.read(path, range)
        //设置响应头 和 内容
        result.headers.forEach { response.setHeader(it.key, Convert.toStr(it.value)) }
        result.status.let { response.status = it }
        result.bytes?.let { response.outputStream.write(it) }
        response.flushBuffer()
    }

    private fun checkFile(path: Path, response: HttpServletResponse): Boolean {
        if (!path.exists()) {
            response.status = HttpServletResponse.SC_NOT_FOUND
            response.flushBuffer()
            return false
        }
        //网站图标请求，直接返回404
        if (path.endsWith("/favicon.ico")) {
            response.status = HttpServletResponse.SC_NOT_FOUND
            response.flushBuffer()
            return false
        }

        if (!path.startsWith(fileProperties.getHomePath())) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.flushBuffer()
            return false
        }
        return true
    }

}