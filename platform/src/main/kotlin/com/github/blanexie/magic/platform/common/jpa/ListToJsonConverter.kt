package com.github.blanexie.magic.platform.common.jpa

import com.alibaba.fastjson.JSON
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter


/**
 *
 * @author xiezc
 * @date 2024/8/30 17:08
 */
@Converter
class ListToJsonConverter : AttributeConverter<ArrayList<*>, String> {

    override fun convertToDatabaseColumn(p0: ArrayList<*>?): String {
        if (p0 == null) {
            return "[]"
        }
        return JSON.toJSONString(p0)
    }

    override fun convertToEntityAttribute(p0: String?): ArrayList<*> {
        if (p0 != null) {
            return arrayListOf<Any>()
        }
        return JSON.parseArray(p0).toCollection(ArrayList())
    }
}