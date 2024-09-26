package com.github.blanexie.magic.service.platform.common.spring

import com.github.blanexie.magic.platform.common.exception.BusinessException
import com.github.blanexie.magic.platform.common.exception.ErrorCode
import com.github.blanexie.magic.platform.common.util.WebResult
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController


/**
 *
 * @author xiezc
 * @date 2024/9/13 17:06
 */
@ControllerAdvice(annotations = [RestController::class])
class GlobalExceptionHandler {

    /**
     * 兜底异常处理
     */
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): WebResult {
        return WebResult.fail(ErrorCode.SYSTEM_ERROR.code, ex.message ?: "Unknown error")
    }

    @ExceptionHandler(BusinessException::class)
    fun handleCustomException(ex: BusinessException): WebResult {
        return WebResult.fail(ex.code, ex.message ?: "Unknown error")
    }
}