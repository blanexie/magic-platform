package com.github.blanexie.magic.service.platform.common.exception


/**
 *
 * @author xiezc
 * @date 2024/9/13 16:58
 */
class BusinessException(val code: Int, message: String, e: Throwable?) : RuntimeException(message, e) {

    constructor(code: Int, message: String) : this(code, message, null)

    constructor(errorCode: ErrorCode) : this(errorCode.code, errorCode.message, null)
}