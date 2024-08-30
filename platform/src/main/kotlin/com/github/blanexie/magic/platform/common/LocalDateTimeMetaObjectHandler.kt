package com.github.blanexie.magic.platform.common

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class LocalDateTimeMetaObjectHandler : MetaObjectHandler {

    override fun insertFill(metaObject: MetaObject) {
        val now = LocalDateTime.now()
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::class.java, now)
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::class.java, now)
    }

    override fun updateFill(metaObject: MetaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject)
    }
}