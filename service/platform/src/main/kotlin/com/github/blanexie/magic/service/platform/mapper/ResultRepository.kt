package com.github.blanexie.magic.service.platform.mapper

import com.github.blanexie.magic.platform.entity.Result
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository


/**
 *
 * @author xiezc
 * @date 2024/8/26 20:14
 */
@Repository
interface ResultRepository : JpaRepository<Result, Int>, JpaSpecificationExecutor<Result> {

}

