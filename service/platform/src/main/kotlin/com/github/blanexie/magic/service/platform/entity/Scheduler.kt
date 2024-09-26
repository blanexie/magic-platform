package com.github.blanexie.magic.service.platform.entity

import jakarta.persistence.*
import java.time.LocalDateTime


/**
 *
 * @author xiezc
 * @date 2024/8/26 19:23
 */
@Entity
@Table
data class Scheduler(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int?,
        var spiderId: Int,      //关联的任务的id
        var url: String,
        var helpUrl: Boolean, //是否只是中间页面，

        var fetchCount: Int,      //被抓取次数， 定时任务 这个值会往上递增

        var extractResult: Boolean, //是否已经提取结果
        var updateTime: LocalDateTime,
        var createTime: LocalDateTime,
)