package com.github.blanexie.magic.service.platform.service.impl

import com.github.blanexie.magic.platform.entity.Result
import com.github.blanexie.magic.platform.mapper.ResultRepository
import com.github.blanexie.magic.platform.service.ResultService
import org.springframework.stereotype.Service

/**
 *
 * @author xiezc
 * @date 2024/8/30 10:57
 */
@Service
class ResultServiceImpl(val resultRepository: ResultRepository) : ResultService {

    override fun save(result: Result) {
        resultRepository.save(result)
    }

}