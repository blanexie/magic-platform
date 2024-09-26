package com.github.blanexie.magic.service.platform.common.jpa

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import jakarta.persistence.AttributeConverter


/**
 *
 * @author xiezc
 * @date 2024/8/30 16:56
 */
class MapToJsonConverter : AttributeConverter<HashMap<String, *>, String> {

    override fun convertToDatabaseColumn(p0: HashMap<String, *>?): String {
        return JSON.toJSONString(p0) ?: "{}"
    }

    override fun convertToEntityAttribute(p0: String?): HashMap<String, String> {
        if (p0 != null) {
            return JSON.parseObject(p0, object : TypeReference<HashMap<String, *>>() {}.type)
        } else {
            return hashMapOf()
        }

    }
}