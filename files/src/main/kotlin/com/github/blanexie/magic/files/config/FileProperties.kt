package com.github.blanexie.magic.files.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.nio.file.Path
import kotlin.io.path.Path

/**
 *
 * @author xiezc
 * @date 2024/9/20 16:09
 */
@Component
@ConfigurationProperties(prefix = "files")
class FileProperties {

    var rangeSize: Long = 1048576
    var home: String? = null

    fun getHomePath(): Path {
        return Path(home!!)
    }

}