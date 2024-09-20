package com.github.blanexie.magic.files.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 *
 * @author xiezc
 * @date 2024/9/20 16:09
 */
@Component
@ConfigurationProperties(prefix = "files")
class FileProperties {
    var home: String? = null
    var rangeSize: Long? = null
}