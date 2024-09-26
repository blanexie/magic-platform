package com.github.blanexie.magic.service.platform.common.exception


/**
 *
 * @author xiezc
 * @date 2024/9/13 17:01
 */
enum class ErrorCode(val code: Int,val message: String) {
    SUCCESS(0, "成功"),
    FAIL(-1, "失败"),
    PARAM_ERROR(10000, "参数错误"),
    SYSTEM_ERROR(10001, "系统异常"),
    ;


}