package com.github.blanexie.magic.platform.entity

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 *
 * @author xiezc
 * @date 2024/8/26 19:47
 */
@Entity
@Table
data class Result(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: String?,
        var schedulerId: Int,
        var content: String,
        var updateTime: LocalDateTime,
        var createTime: LocalDateTime,
) {

}