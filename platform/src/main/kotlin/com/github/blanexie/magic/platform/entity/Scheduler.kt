package com.github.blanexie.magic.platform.entity

import com.baomidou.mybatisplus.annotation.*
import java.time.LocalDateTime


/**
 *
 * @author xiezc
 * @date 2024/8/26 19:23
 */
@TableName("schedules")
data class Scheduler(
        @TableId(type = IdType.AUTO)
        var id: Int?,
        var spiderId: Int,      //关联的任务的id
        var url: String,
        var helpUrl: Boolean, //是否只是中间页面，

        var fetchCount: Int,      //被抓取次数， 定时任务 这个值会往上递增

        var extractResult: Boolean, //是否已经提取结果
        @TableField(fill = FieldFill.INSERT_UPDATE)
        var updateTime: LocalDateTime,
        @TableField(fill = FieldFill.INSERT)
        var createTime: LocalDateTime,
)