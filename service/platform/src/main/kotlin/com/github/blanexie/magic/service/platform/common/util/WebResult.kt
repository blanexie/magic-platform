package com.github.blanexie.magic.service.platform.common.util

import com.github.blanexie.magic.platform.common.exception.ErrorCode


/**
 *
 * @author xiezc
 * @date 2024/8/30 19:29
 */
data class WebResult(
        val code: Int = 2000,
        val errMsg: String? = null,
        val data: Any? = null,
) {
    companion object {
        fun <T> success(data: T?): WebResult {
            return WebResult(data = data)
        }

        fun fail(code: Int, errMsg: String): WebResult {
            return WebResult(code, errMsg = errMsg)
        }

        fun <T> fail(errorCode: ErrorCode): WebResult {
            return WebResult(errorCode.code, errMsg = errorCode.message)
        }

    }
}