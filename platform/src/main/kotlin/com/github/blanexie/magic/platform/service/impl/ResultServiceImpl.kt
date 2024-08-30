package com.github.blanexie.magic.platform.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.github.blanexie.magic.platform.entity.Result
import com.github.blanexie.magic.platform.mapper.ResultMapper
import com.github.blanexie.magic.platform.service.ResultService
import org.springframework.stereotype.Service

/**
 *
 * @author xiezc
 * @date 2024/8/30 10:57
 */
@Service
class ResultServiceImpl : ServiceImpl<ResultMapper, Result>(), ResultService {}