package com.github.blanexie.magic.service.files.util

object ContentTypeUtil {

    private val contentTypeMap = mapOf(
            "txt" to "text/plain",
            "html" to "text/html",
            "css" to "text/css",
            "js" to "text/javascript",
            "jpg" to "image/jpeg",
            "jpeg" to "image/jpeg",
            "png" to "image/png",
            "gif" to "image/gif",
            "pdf" to "application/pdf",
            "doc" to "application/msword",
            "docx" to "application/vnd.openxmlformats - officedocument.wordprocessingml.document",
            "xls" to "application/vnd.ms - excel",
            "xlsx" to "application/vnd.openxmlformats - officedocument.spreadsheetml.sheet",
            "mp3" to "audio/mpeg",
            "mpeg" to "video/mpeg",
            "mp4" to "video/mp4",
            "json" to "application/json",
            "xml" to "application/xml",
            "zip" to "application/zip"
    )

    fun getContentTypeByExtension(extension: String): String {
        return contentTypeMap[extension.lowercase()] ?: "text/plain"
    }
}