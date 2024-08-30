package com.github.blanexie.magic.platform.entity

import com.baomidou.mybatisplus.annotation.*
import java.time.LocalDateTime

/**
 *
 * @author xiezc
 * @date 2024/8/26 19:47
 */
@TableName("result")
data class Result(
        @TableId(type = IdType.AUTO)
        var id: String?,
        var schedulerId: Int,
        var content: String,
        @TableField(fill = FieldFill.INSERT_UPDATE)
        var updateTime: LocalDateTime,
        @TableField(fill = FieldFill.INSERT)
        var createTime: LocalDateTime,
)